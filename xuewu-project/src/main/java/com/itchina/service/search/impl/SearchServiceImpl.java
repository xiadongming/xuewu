package com.itchina.service.search.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.itchina.base.HouseSort;
import com.itchina.base.RentValueBlock;
import com.itchina.entity.House;
import com.itchina.entity.HouseDetail;
import com.itchina.entity.HouseTag;
import com.itchina.entity.SupportAddress;
import com.itchina.form.RentSearch;
import com.itchina.repository.HouseDetailRepository;
import com.itchina.repository.HouseRepository;
import com.itchina.repository.HouseTagRspository;
import com.itchina.repository.SupportAddressReposity;
import com.itchina.service.IAddressService;
import com.itchina.service.ServiceMultiResult;
import com.itchina.service.ServiceResult;
import com.itchina.service.search.ISearchService;
import com.itchina.service.search.constants.*;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.RequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.transport.TransportResponse;
import org.hibernate.engine.internal.Collections;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: xiadongming
 * @Date: 2020/8/17 21:30
 */
@Service
public class SearchServiceImpl implements ISearchService {
    final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final String INDEX_NAME = "xuewu";

    private static final String INDEX_TYPE = "house";

    private static final String INDEX_TOPIC = "house_build";


    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HouseDetailRepository houseDetailRepository;
    @Autowired
    private HouseTagRspository houseTagRspository;
    @Autowired
    private TransportClient esClient;
    @Autowired
    private SupportAddressReposity supportAddressReposity;
    @Autowired
    private IAddressService addressService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = INDEX_TOPIC)
    private void handleMessage(String content) {
        HouseIndexMessage message = null;
        try {
            message = objectMapper.readValue(content, HouseIndexMessage.class);
            switch (message.getOperation()) {
                case HouseIndexMessage.INDEX:
                    this.createOrUpdateIndex(message);
                    break;
                case HouseIndexMessage.REMOVE:
                    this.removeIndex(message);
                    break;
                default:
                    logger.info("不支持的异步操作类型：content＝　" + content);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeIndex(HouseIndexMessage message) {
        Integer houseId = message.getHouseId();
        DeleteRequestBuilder deleteRequestBuilder = esClient.prepareDelete(INDEX_NAME, INDEX_TYPE, String.valueOf(houseId));
        DeleteResponse response = deleteRequestBuilder.get();

        logger.info("删除索引状态：status= " + response.status().getStatus());

        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient).filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId)).source(INDEX_NAME);
        BulkByScrollResponse response2 = deleteByQueryRequestBuilder.get();
        long deleted = response2.getDeleted();
        logger.info("删除索引数据量：delete= " + deleted);
        if (deleted <= 0) {
            this.remove(houseId, message.getRetry() + 1);
        }
    }

    private void remove(Integer houseId, int retry) {
        if (retry >= HouseIndexMessage.MAX_RETRY) {
            logger.error("删除索引次数大于最大次数：retry= " + HouseIndexMessage.MAX_RETRY);
            return;
        }
        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.REMOVE, retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("kafka删除索引报错");
            e.printStackTrace();
        }
    }

    private void createOrUpdateIndex(HouseIndexMessage message) throws JsonProcessingException {
        Integer houseId = message.getHouseId();
        House house = houseRepository.findOne(houseId);
        if (null == house) {
            logger.error("查询房屋信息为空:houseId= " + houseId);
            this.index(houseId, message.getRetry() + 1);//重新入队列
            return;
        }
        HouseIndexTemplate houseIndexTemplate = modelMapper.map(house, HouseIndexTemplate.class);
        HouseDetail houseDetail = houseDetailRepository.findByHouseId(houseId);
        if (null == houseDetail) {
            houseDetail = houseDetailRepository.findByHouseId(27);
        }

        modelMapper.map(houseDetail, houseIndexTemplate);

        SupportAddress city = supportAddressReposity.findByEnNameAndLevel(house.getCityEnName(), SupportAddress.Level.CITY.getValue());
        SupportAddress region = supportAddressReposity.findByEnNameAndLevel(house.getRegionEnName(), SupportAddress.Level.CITY.getValue());
        if (null == region) {
            region = supportAddressReposity.findByEnNameAndLevel("bj", SupportAddress.Level.CITY.getValue());
        }

        String address = city.getCnName() + region.getCnName() + house.getStreet() + house.getDistrict() + houseDetail.getDetailAddress();

        ServiceResult<BaiduMapLocation> location = addressService.getBaiduMapLocation(city.getCnName(), address);
        if (!location.isSuccess()) {
            this.index(message.getHouseId(), message.getRetry() + 1);
            return;
        }
        houseIndexTemplate.setBaiduMapLocation(location.getResult());

        List<HouseTag> houseTagList = houseTagRspository.findAllByHouseId(houseId);
        if (!CollectionUtils.isEmpty(houseTagList)) {
            List<String> strings = new ArrayList<>();
            for (HouseTag houseTag : houseTagList) {
                strings.add(houseTag.getName());
            }
            houseIndexTemplate.setTags(strings);
        }
        //查询es中是否存在该数据
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));

        logger.info("从es中查询到的数据：JsonsearchRequestBuilder= " + JSONObject.fromObject(searchRequestBuilder).toString());
        logger.info("从es中查询到的数据：searchRequestBuilder= " + searchRequestBuilder.toString());
        SearchResponse response = searchRequestBuilder.get();
        long totalHits = response.getHits().getTotalHits();
        logger.info("es中的数据量：totalHits= " + totalHits);
        if (Integer.valueOf(String.valueOf(totalHits)) > 0) {
            logger.info("es中的数据量：getHits= " + response.getHits());
            logger.info("es中的数据量：getAt= " + response.getHits().getAt(0));
            logger.info("es中的数据量：getSourceAsMap= " + response.getHits().getAt(0).getSourceAsMap());
            logger.info("es中的数据量：getSourceAsString= " + response.getHits().getAt(0).getSourceAsString());

            logger.info("==============================");
            logger.info("es中的数据量：getSource= " + JSONObject.fromObject(response.getHits().getAt(0).getSource()));
            logger.info("es中的数据量：getSourceAsMap= " + JSONObject.fromObject(response.getHits().getAt(0).getSourceAsMap()));
            logger.info("es中的数据量：getSourceAsString= " + JSONObject.fromObject(response.getHits().getAt(0).getSourceAsString()));
        }
        boolean success;
        if (0 == totalHits) {
            // 创建
            success = create(houseIndexTemplate);
        } else if (1 == totalHits) {
            //更新
            String id = response.getHits().getAt(0).getId();
            success = update(id, houseIndexTemplate);

        } else {
            success = deleteAndUpdate(totalHits, houseIndexTemplate);
        }
        //create()
        //update();
        //deneteAnfUpdate();
        logger.info("操作索引es结果：success= " + success);
    }

    private void index(Integer houseId, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("重新建立索引次数大于：retry= " + HouseIndexMessage.MAX_RETRY);
            return;
        }
        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.INDEX, retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("重新建立索引异常：houseId= " + houseId);
            e.printStackTrace();
        }
    }

    @Override
    public void index(Integer houseId) throws JsonProcessingException {
        this.index(houseId, 0);
    }

    public boolean create(HouseIndexTemplate houseIndexTemplate) {
        if (!updateSuggest(houseIndexTemplate)) {
            return false;
        }
        try {
            //创建索引
            IndexResponse response = esClient.prepareIndex(INDEX_NAME, INDEX_TYPE)
                    .setSource(objectMapper.writeValueAsBytes(houseIndexTemplate), XContentType.JSON)//source需要是json格式
                    .get();
            if (response.status() == RestStatus.CREATED) {
                return true;
            } else {
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error("创建索引出错：houseId= " + houseIndexTemplate.getHouseId());
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String esId, HouseIndexTemplate houseIndexTemplate) throws JsonProcessingException {
        if (!updateSuggest(houseIndexTemplate)) {
            return false;
        }
        //更新数据
        UpdateResponse response = esClient.prepareUpdate(INDEX_NAME, INDEX_TYPE, esId).setDoc(objectMapper.writeValueAsBytes(houseIndexTemplate),
                XContentType.JSON).get();
        if (response.status() == RestStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAndUpdate(long totalHit, HouseIndexTemplate houseIndexTemplate) {
        //删除索引的api
        // DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
        //        .newRequestBuilder(esClient).setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndexTemplate.getHouseId())).setSource(INDEX_NAME);
        // .newRequestBuilder(esClient).setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndexTemplate.getHouseId()))
        //.filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndexTemplate.getHouseId()))
        // .source(INDEX_NAME);
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndexTemplate.getHouseId()))
                .source(INDEX_NAME);
        logger.info("builder= " + builder);
        BulkByScrollResponse bulkByScrollResponse = builder.get();
        long totalDeleted = bulkByScrollResponse.getDeleted();
        if (Long.parseLong(String.valueOf(totalDeleted)) != totalHit) {
            logger.error("删除数量和传入的数量不相等");
            return false;
        } else {
            return create(houseIndexTemplate);
        }
    }

    @Override
    public void remove(Integer houseId) {
        this.remove(houseId, 0);
    }

    @Override
    public ServiceMultiResult<Integer> query(RentSearch rentSearch) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, rentSearch.getCityEnName());


        if (null != rentSearch.getRegionEnName() && !"*".equals(rentSearch.getRegionEnName())) {
            TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, rentSearch.getRegionEnName());
            boolQueryBuilder.filter(termQueryBuilder1);
        }
        //房屋面积
        RentValueBlock area = RentValueBlock.matchArea(rentSearch.getAreaBlock());
        if (!RentValueBlock.ALL.equals(area)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexKey.AREA);
            if (area.getMax() > 0) {
                rangeQueryBuilder.lte(area.getMax());
            }
            if (area.getMin() > 0) {
                rangeQueryBuilder.gte(area.getMin());
            }
            //添加到boolBuilder
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        //房屋价格
        RentValueBlock price = RentValueBlock.matchPrice(rentSearch.getPriceBlock());
        if (!RentValueBlock.ALL.equals(price)) {
            RangeQueryBuilder priceQueryBuilder = QueryBuilders.rangeQuery(HouseIndexKey.PRICE);
            if (price.getMax() > 0) {
                priceQueryBuilder.lte(area.getMax());
            }
            if (price.getMin() > 0) {
                priceQueryBuilder.gte(area.getMin());
            }
            //添加到boolBuilder
            boolQueryBuilder.filter(priceQueryBuilder);
        }
        //房屋朝向
        if (rentSearch.getDirection() > 0) {
            TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(HouseIndexKey.DIRECTION, rentSearch.getDirection());
            boolQueryBuilder.filter(termQueryBuilder1);
        }
        //租用方式：
        if (rentSearch.getRentWay() > -1) {
            TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery(HouseIndexKey.RENT_WAY, rentSearch.getRentWay());
            boolQueryBuilder.filter(termQueryBuilder1);
        }


        //搜索的关键字段
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(rentSearch.getKeywords(),
                HouseIndexKey.TITLE,
                HouseIndexKey.TRAFFIC,
                HouseIndexKey.DISTRICT,
                HouseIndexKey.ROUND_SERVICE,
                HouseIndexKey.SUBWAY_LINE_NAME,
                HouseIndexKey.SUBWAY_STATION_NAME);

        //将搜索条件添加到boolQueryBuilder中
        boolQueryBuilder.filter(termQueryBuilder);
        boolQueryBuilder.must(multiMatchQueryBuilder);

        SearchRequestBuilder searchRequestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .addSort(HouseSort.getSortKey(rentSearch.getOrderBy()), SortOrder.fromString(rentSearch.getOrderDirection()))
                .setFrom(rentSearch.getStart())
                .setSize(rentSearch.getSize());
        SearchResponse response = searchRequestBuilder.get();
        List<Integer> houseIds = new ArrayList<>();
        if (RestStatus.OK != response.status()) {
            logger.error("es查询异常");
            return new ServiceMultiResult<>(0, houseIds);
        }

        for (SearchHit hit : response.getHits()) {
            houseIds.add(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
            logger.info("es查询结果= " + JSONObject.fromObject(hit.getSourceAsMap()).toString());
        }

        return new ServiceMultiResult<>(response.getHits().getTotalHits(), houseIds);
    }

    @Override
    public ServiceResult<List<String>> suggest(String prefix) {
        //自动补全功能
        CompletionSuggestionBuilder suggestion = SuggestBuilders.completionSuggestion("suggest").prefix(prefix).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestion);

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .suggest(suggestBuilder);
        logger.info("requestBuilder= " + requestBuilder.toString());
        SearchResponse response = requestBuilder.get();
        for (SearchHit hit : response.getHits()) {
            System.out.println("============******=================");
            System.out.println("自动补全的结果是： suggest= " + JSONObject.fromObject(hit.getSourceAsMap()).toString());

        }
        Suggest suggest = response.getSuggest();
        if (suggest == null) {
            return ServiceResult.of(new ArrayList<>());
        }

        Suggest.Suggestion result = suggest.getSuggestion("autocomplete");
        int maxSugest = 0;
        HashSet<String> suggestSet = new HashSet<>();
        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                if (item.getOptions().isEmpty()) {
                    continue;
                }
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (suggestSet.contains(tip)) {
                        continue;
                    }
                    suggestSet.add(tip);
                    maxSugest++;
                }
            }
            if (maxSugest >= 10) {
                break;
            }
        }
        List<String> suggestList = Lists.newArrayList(suggestSet.toArray(new String[]{}));

        return ServiceResult.of(suggestList);
    }

    @Override
    public ServiceResult<Long> aggregateDistrictHouse(String cityEnName, String regionEnName, String district) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder cityEnNameBuilder = QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName);
        TermQueryBuilder regionEnNameBuilder = QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, regionEnName);
        TermQueryBuilder districtBuilder = QueryBuilders.termQuery(HouseIndexKey.DISTRICT, district);

        boolQueryBuilder.filter(cityEnNameBuilder).filter(regionEnNameBuilder).filter(districtBuilder);

        //聚合条件
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(HouseIndexKey.AGG_DISTRICT);//聚合的结果是HouseIndexKey.AGG_DISTRICT
        termsAggregationBuilder.field(HouseIndexKey.DISTRICT);//聚合的 字段是HouseIndexKey.DISTRICT

        SearchRequestBuilder searchRequestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .addAggregation(termsAggregationBuilder)//聚合操作
                .setSize(0);//只需要聚合数据
        logger.info("聚合统计  searchRequestBuilder= " + searchRequestBuilder.toString());
        SearchResponse response = searchRequestBuilder.get();
        if (response.status() == RestStatus.OK) {
            Terms aggregation = response.getAggregations().get(HouseIndexKey.AGG_DISTRICT);
            if (null != aggregation.getBuckets() && !aggregation.getBuckets().isEmpty()) {
                return ServiceResult.of(aggregation.getBucketByKey(district).getDocCount());
            }
            logger.info("聚合数据：aggregation.getBucketByKey(district).getDocCount()= " + aggregation.getBucketByKey(district).getDocCount());
        } else {
            logger.error("聚合失败");
            return ServiceResult.of(0L);
        }
        return null;
    }

    @Override
    public ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName);
        //聚合条件，设置结果，和 变量
        TermsAggregationBuilder aggBuilders = AggregationBuilders.terms(HouseIndexKey.AGG_REGION);
        aggBuilders.field(HouseIndexKey.REGION_EN_NAME);

        SearchRequestBuilder searchRequestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .addAggregation(aggBuilders);
        logger.info("地图聚合的信息：searchRequestBuilder= " + searchRequestBuilder.toString());
        SearchResponse response = searchRequestBuilder.get();
        ArrayList<HouseBucketDTO> buckts = new ArrayList<>();
        if (RestStatus.OK != response.status()) {
            logger.error("地图功能，聚合失败");
            return new ServiceMultiResult<>(0, buckts);
        }
        //获取目标数据
        Terms aggregation = response.getAggregations().get(HouseIndexKey.AGG_REGION);
        for (Terms.Bucket bucket : aggregation.getBuckets()) {
            HouseBucketDTO houseBucketDTO = new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount());
            buckts.add(houseBucketDTO);
        }
        return new ServiceMultiResult<>(response.getHits().getTotalHits(), buckts);
    }

    @Override
    public ServiceMultiResult<Integer> mapQuery(String cityEnName, String orderBy, String orderDirection, int start, int size) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName);
        boolQueryBuilder.filter(termQueryBuilder);
        SearchRequestBuilder searchRequestBuilder = this.esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
                                                                .setQuery(boolQueryBuilder)
                                                                .addSort(HouseSort.getSortKey(orderBy), SortOrder.fromString(orderDirection))
                                                                .setFrom(start)
                                                                .setSize(size);
        SearchResponse searchResponse = searchRequestBuilder.get();
        ArrayList<Integer> integers = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            integers.add(Integer.valueOf(String.valueOf(hit.getSource().get(HouseIndexKey.HOUSE_ID))));
        }
        return new ServiceMultiResult<>(searchResponse.getHits().getTotalHits(),integers);
    }

    @Override
    public ServiceMultiResult<Integer> mapQuery(MapSearch mapSearch) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, mapSearch.getCityEnName()));

        boolQuery.filter(
                QueryBuilders.geoBoundingBoxQuery("location")
                        .setCorners(
                                new GeoPoint(mapSearch.getLeftLatitude(), mapSearch.getLeftLongitude()),
                                new GeoPoint(mapSearch.getRightLatitude(), mapSearch.getRightLongitude())
                        ));

        SearchRequestBuilder builder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQuery)
                .addSort(HouseSort.getSortKey(mapSearch.getOrderBy()),
                        SortOrder.fromString(mapSearch.getOrderDirection()))
                .setFrom(mapSearch.getStart())
                .setSize(mapSearch.getSize());

        List<Integer> houseIds = new ArrayList<>();
        SearchResponse response = builder.get();
        if (RestStatus.OK != response.status()) {
            logger.warn("Search status is not ok for " + builder);
            return new ServiceMultiResult<>(0, houseIds);
        }

        for (SearchHit hit : response.getHits()) {
            houseIds.add(Integer.valueOf(String.valueOf(hit.getSource().get(HouseIndexKey.HOUSE_ID))));
        }
        return new ServiceMultiResult<>(response.getHits().getTotalHits(), houseIds);
    }

    private boolean updateSuggest(HouseIndexTemplate houseIndexTemplate) {
        //进行分词
        AnalyzeRequestBuilder requestBuilder = new AnalyzeRequestBuilder(this.esClient, AnalyzeAction.INSTANCE, INDEX_NAME,
                houseIndexTemplate.getTitle(),//以下是需要分词的字段
                houseIndexTemplate.getLayoutDesc(),
                houseIndexTemplate.getRoundService(),
                houseIndexTemplate.getDescription(), houseIndexTemplate.getSubwayLineName(),
                houseIndexTemplate.getSubwayStationName());
        requestBuilder.setAnalyzer("ik_smart");
        AnalyzeResponse responseTokens = requestBuilder.get();
        //tokens是分词以后的结果
        List<AnalyzeResponse.AnalyzeToken> tokens = responseTokens.getTokens();
        logger.info("ik分词的：tokens=  " + JSONArray.fromObject(tokens).toString());
        if (null == tokens) {
            logger.error("ik分词器出现异常：houseId= " + houseIndexTemplate.getHouseId());
            return false;
        }
        ArrayList<HouseSuggest> suggests = new ArrayList<>();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            //num相当于null
            if ("<NUM>".equals(token.getType()) || token.getTerm().length() < 2) {
                continue;
            }
            HouseSuggest houseSuggest = new HouseSuggest();
            houseSuggest.setInput(token.getTerm());
            suggests.add(houseSuggest);
        }
        //小区名称进行补全
        HouseSuggest suggest = new HouseSuggest();
        suggest.setInput(houseIndexTemplate.getDistrict());
        suggests.add(suggest);
        houseIndexTemplate.setSuggest(suggests);
        return true;
    }

}
