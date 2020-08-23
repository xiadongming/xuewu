package com.itchina.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.itchina.base.HouseSort;
import com.itchina.base.HouseStatus;
import com.itchina.base.LoginUserUtils;
import com.itchina.dto.HouseDTO;
import com.itchina.dto.HouseDetailDTO;
import com.itchina.dto.HousePictureDTO;
import com.itchina.entity.*;
import com.itchina.form.DatatableSearch;
import com.itchina.form.HouseForm;
import com.itchina.form.PhotoForm;
import com.itchina.form.RentSearch;
import com.itchina.repository.*;
import com.itchina.service.*;
import com.itchina.service.search.ISearchService;
import com.itchina.service.search.constants.MapSearch;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.html.HTMLDocument;
import java.util.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
@Service
public class HouseServiceImpl implements IHouseService {
    final Logger logger = LoggerFactory.getLogger(HouseServiceImpl.class);

    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private HouseDetailRepository houseDetailRepository;
    @Autowired
    private HousePictureRepository housePictureRepository;
    @Autowired
    private HouseSubscribeRspository houseSubscribeRspository;
    @Autowired
    private HouseTagRspository houseTagRspository;
    @Autowired
    private ISubwayService subwayService;
    @Autowired
    private ISubwayStationService subwayStationService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ISearchService searchService;


    @Override
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {

        House house = modelMapper.map(houseForm, House.class);
        house.setCreateTime(new Date());
        house.setLastUpdateTime(new Date());
        house.setAdminId(Integer.valueOf(String.valueOf(LoginUserUtils.getLoginUserInfo().getId())));//当前操作用户的id，从security中获取
        house = houseRepository.save(house);

        HouseDetail houseDetail = new HouseDetail();
        ServiceResult<HouseDTO> houseDTOServiceResult = wrapperDetailInfo(houseDetail, houseForm);
        if (null != houseDTOServiceResult) {
            return houseDTOServiceResult;
        }

        houseDetail.setHouseId(house.getId());
        houseDetail = houseDetailRepository.save(houseDetail);

        //图片
        List<HousePicture> housePictureList = getPicture(houseForm, house.getId());
        Iterable<HousePicture> pictursItera = housePictureRepository.save(housePictureList);
        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(houseDetail, HouseDetailDTO.class);
        houseDTO.setHouseDetail(houseDetailDTO);

        List<HousePictureDTO> housePictureDTOS = new ArrayList<>();

        for (HousePicture picture : pictursItera) {
            HousePictureDTO map = modelMapper.map(picture, HousePictureDTO.class);
            housePictureDTOS.add(map);
        }
        houseDTO.setPictures(housePictureDTOS);
        houseDTO.setCover(houseDTO.getCover());

        //tags
        List<HouseTag> tagList = null;
        List<String> tags = houseForm.getTags();
        if (!CollectionUtils.isEmpty(tags)) {
            tagList = new ArrayList<>();
            for (String tag : tags) {
                tagList.add(new HouseTag(house.getId(), tag));
            }
            houseTagRspository.save(tagList);
            houseDTO.setTags(tags);
        }
        return new ServiceResult<HouseDTO>(true, null, houseDTO);
    }

    @Override
    public ServiceMultiResult<HouseDTO> admingQWuery(DatatableSearch searchBody) {
    /*    List<HouseDTO> houseList = new ArrayList<>();
        Iterable<House> all = houseRepository.findAll();
        HouseDTO houseDTO = null;
        for (House house : all) {
            houseDTO =  modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(house.getCover());
            houseList.add(houseDTO);
        }*/
        // 分页如下-----------
        List<HouseDTO> houseList = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.fromString(searchBody.getDirection()), searchBody.getOrderBy());
        int page = searchBody.getStart() / searchBody.getLength();
        logger.info("page= " + page + ", searchBody.getLength()= " + searchBody.getLength() + ", searchBody.getStart()= " + searchBody.getStart());
        PageRequest pageRequest = new PageRequest(page, searchBody.getLength(), sort);
        //封装查询条件
        Specification<House> specification = new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("adminId"), LoginUserUtils.getLoginUserInfo().getId());
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get("status"), HouseStatus.DELETED.getValue()));
                if (null != searchBody.getCity()) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cityEnName"), searchBody.getCity()));
                }
                if (null != searchBody.getStatus()) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), searchBody.getStatus()));
                }
                if (null != searchBody.getCreateTimeMin()) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMin()));
                }
                if (null != searchBody.getCreateTimeMax()) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMax()));
                }
                if (null != searchBody.getTitle()) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + searchBody.getTitle() + "%"));
                }

                return predicate;
            }
        };

        //Page<House> all = houseRepository.findAll(pageRequest);
        Page<House> all = houseRepository.findAll(specification, pageRequest);
        logger.info("all= " + all);
        HouseDTO houseDTO = null;
        for (House house : all) {
            houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(house.getCover());
            houseList.add(houseDTO);
        }
        return new ServiceMultiResult<>(houseList.size(), houseList);
    }

    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        logger.info("编辑后屋信息入参：id= " + id);
        Integer intId = Integer.valueOf(String.valueOf(id));
        House house = houseRepository.findOne(intId);
        if (null == house) {
            return new ServiceResult(false);
        }

        HouseDetail houseDetail = houseDetailRepository.findByHouseId(intId);
        if (null == houseDetail) {
            houseDetail = houseDetailRepository.findByHouseId(34);
        }
        List<HousePicture> housePictureList = housePictureRepository.findAllByHouseId(intId);
        HouseDetailDTO houseDetailDTO = modelMapper.map(houseDetail, HouseDetailDTO.class);
        List<HousePictureDTO> pictureDTOList = new ArrayList<>();
        HousePictureDTO housePictureDTO = null;
        for (HousePicture housePicture : housePictureList) {
            housePictureDTO = modelMapper.map(housePicture, HousePictureDTO.class);
            pictureDTOList.add(housePictureDTO);

        }
        List<HouseTag> tagList = houseTagRspository.findAllByHouseId(intId);
        List<String> tagNameList = new ArrayList<>();
        for (HouseTag houseTag : tagList) {
            tagNameList.add(houseTag.getName());
        }
        HouseDTO result = modelMapper.map(house, HouseDTO.class);
        result.setHouseDetail(houseDetailDTO);
        result.setPictures(pictureDTOList);
        result.setTags(tagNameList);
        return ServiceResult.of(result);
    }

    @Override
    public ServiceResult<HouseDTO> update(HouseForm houseForm) {


        return null;
    }

    @Override
    @Transactional
    public ServiceResult updateStatus(Long id, int status) {
        logger.info("后台房屋审核接口入参：id= " + id + ", status= " + status);
        Integer intId = Integer.valueOf(String.valueOf(id));
        House house = houseRepository.findOne(intId);
        if (null == house) {
            return ServiceResult.notFound();
        }
        if (house.getStatus() == status) {
            return new ServiceResult(false, "状态值和数据库一致，不需要改变");
        }
        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult(false, "该房屋已经出租，不允许修改状态");
        }
        if (house.getStatus() == HouseStatus.DELETED.getValue()) {
            return new ServiceResult(false, "改房屋信息已经删除");
        }

        try {
            houseRepository.updateStatus(intId, status);
            //int a = 100 / 0;
            //创建索引
            if (status == HouseStatus.PASSES.getValue()) {
                searchService.index(intId);
            } else {
                searchService.remove(intId);
            }

        } catch (Exception e) {
            logger.error("审核房屋出错");
            e.printStackTrace();
            throw new RuntimeException();
        }
        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
        //Sort sort = new Sort(Sort.Direction.DESC, "lastUpdateTime");
        //从es中获取
        if (StringUtils.isNotBlank(rentSearch.getKeywords())) {
            ServiceMultiResult<Integer> houseIdsResult = searchService.query(rentSearch);
            if (null == houseIdsResult && CollectionUtils.isEmpty(houseIdsResult.getResult())) {
                return new ServiceMultiResult<>(0, new ArrayList<>());
            }
            logger.info("从es中查询数据");

            return new ServiceMultiResult<HouseDTO>(houseIdsResult.getTotal(), wrapperHouseResult(houseIdsResult.getResult()));
        }

        //从数据库查出来以后，应该放到es中
        ServiceMultiResult<HouseDTO> houseDTOServiceMultiResult = dbSimpleQuery(rentSearch);
        putDBDataToEs(houseDTOServiceMultiResult);

        return houseDTOServiceMultiResult;
    }

    @Override
    public ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch) {
        ServiceMultiResult<Integer> longServiceMultiResult = searchService.mapQuery(mapSearch.getCityEnName(), mapSearch.getOrderBy(), mapSearch.getOrderDirection(), mapSearch.getStart(), mapSearch.getSize());
        if(0 == longServiceMultiResult.getTotal()){
            return new ServiceMultiResult<>(0,new ArrayList<>());
        }
        List<HouseDTO> houseDTOS = wrapperHouseResult(longServiceMultiResult.getResult());
        return new ServiceMultiResult<>(longServiceMultiResult.getTotal(),houseDTOS);
    }

    @Override
    public ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch) {
        ServiceMultiResult<Integer> serviceResult = searchService.mapQuery(mapSearch);
        if (serviceResult.getTotal() == 0) {
            return new ServiceMultiResult<>(0, new ArrayList<>());
        }

        List<HouseDTO> houses = wrapperHouseResult(serviceResult.getResult());
        return new ServiceMultiResult<>(serviceResult.getTotal(), houses);
    }

    private void putDBDataToEs(ServiceMultiResult<HouseDTO> houseDTOServiceMultiResult) {
        logger.info("将从db中查询到的数据放到es中");
        if (null != houseDTOServiceMultiResult && !CollectionUtils.isEmpty(houseDTOServiceMultiResult.getResult())) {
            for (HouseDTO houseDTO : houseDTOServiceMultiResult.getResult()) {
                try {
                    searchService.index(houseDTO.getId());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ServiceMultiResult<HouseDTO> dbSimpleQuery(RentSearch rentSearch) {
        logger.info("从db中查询数据");
        Sort sort = HouseSort.generateSort(rentSearch.getOrderBy(), rentSearch.getOrderDirection());
        int pageNo = rentSearch.getStart() / rentSearch.getSize();
        PageRequest pageAble = new PageRequest(pageNo, rentSearch.getSize(), sort);
        Specification<House> specification = new Specification<House>() {
            @Override
            public Predicate toPredicate(Root<House> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("status"), HouseStatus.PASSES.getValue());
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cityEnName"), rentSearch.getCityEnName()));
                if (HouseSort.distance_to_subway_key.equals(rentSearch.getOrderBy())) {
                    //距离地铁站数量大于-1
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.gt(root.get(HouseSort.distance_to_subway_key), -1));
                }
                return predicate;
            }
        };
        Page<House> all = houseRepository.findAll(specification, pageAble);

        List<Integer> houseIds = new ArrayList<>();
        HashMap<Integer, HouseDTO> objectObjectHashMap = Maps.newHashMap();
        List<HouseDTO> houseDtoList = new ArrayList<>();
        for (House house : all) {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(house.getCover());
            houseDtoList.add(houseDTO);

            houseIds.add(house.getId());
            objectObjectHashMap.put(house.getId(), houseDTO);
        }

        wrapperHouseList(houseIds, objectObjectHashMap);
        logger.info("JSONhouseDtoList= " + JSONArray.fromObject(houseDtoList).toString());
        return new ServiceMultiResult<>(all.getTotalElements(), houseDtoList);
    }

    private List<HouseDTO> wrapperHouseResult(List<Integer> houseIds) {
        ArrayList<HouseDTO> result = new ArrayList<>();
        HashMap<Integer, HouseDTO> longHouseDTOHashMap = new HashMap<>();
        Iterable<House> allHouse = houseRepository.findAll(houseIds);
        HouseDTO houseDTO = null;
        for (House house : allHouse) {
            houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(house.getCover());
            longHouseDTOHashMap.put(house.getId(), houseDTO);
        }
        wrapperHouseList(houseIds, longHouseDTOHashMap);
        //矫正顺序
        for (Integer houseId : houseIds) {
            result.add(longHouseDTOHashMap.get(houseId));
        }
        return result;
    }


    private void wrapperHouseList(List<Integer> ids, Map<Integer, HouseDTO> idToHouseMap) {
        List<HouseDetail> details = houseDetailRepository.findAllByHouseIdIn(ids);
        HouseDTO houseDTO = null;
        HouseDetailDTO houseDetailDTO = null;
        for (HouseDetail detail : details) {
            houseDTO = idToHouseMap.get(detail.getHouseId());
            houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);

            houseDTO.setHouseDetail(houseDetailDTO);
        }
        List<HouseTag> houseTags = houseTagRspository.findAllByHouseIdIn(ids);
        for (HouseTag houseTag : houseTags) {
            HouseDTO houseDTO1 = idToHouseMap.get(houseTag.getHouseId());
            List<String> tagList = new ArrayList<String>();
            tagList.add(houseTag.getName());
            houseDTO1.setTags(tagList);
        }

    }

    private List<HousePicture> getPicture(HouseForm houseForm, Integer houseId) {
        List<HousePicture> housePictureList = new ArrayList<>();
        if (null == houseForm.getPhotos() || houseForm.getPhotos().isEmpty()) {
            return housePictureList;
        }
        for (PhotoForm photo : houseForm.getPhotos()) {
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(photo.getPath());
            picture.setPath(photo.getPath());
            picture.setWidth(photo.getWidth());
            picture.setHeight(photo.getHeight());
            housePictureList.add(picture);
        }
        return housePictureList;
    }

    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) {
        Subway subway = subwayService.findOne(houseForm.getSubwayLineId());
        if (null == subway) {
            return new ServiceResult<HouseDTO>(false, "参数有误");
        }
        SubwayStation subwayStation = subwayStationService.findOne(houseForm.getSubwayStationId());
        if (null == subwayStation || subway.getId() != subwayStation.getSubwayId()) {
            return new ServiceResult<HouseDTO>(false, "参数有误");
        }
        houseDetail.setSubwayLineId(subway.getId());
        houseDetail.setSubwayLineName(subway.getName());
        houseDetail.setSubwayStationId(subwayStation.getId());
        houseDetail.setSubwayStationName(subwayStation.getName());
        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        return null;
    }

}
