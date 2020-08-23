package com.itchina.service.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itchina.form.RentSearch;
import com.itchina.service.ServiceMultiResult;
import com.itchina.service.ServiceResult;
import com.itchina.service.search.constants.HouseBucketDTO;
import com.itchina.service.search.constants.MapSearch;

import java.util.List;

/**
 * @Author: xiadongming
 * @Date: 2020/8/17 21:28
 */
public interface ISearchService {
    //创建索引
    void index(Integer houseId) throws JsonProcessingException;

    //删除索引
    void remove(Integer houseId);

    //从es中国查询数据
    ServiceMultiResult<Integer> query(RentSearch rentSearch);

    //自动补全
    ServiceResult<List<String>> suggest(String prefix);

    //聚合功能
    ServiceResult<Long> aggregateDistrictHouse(String cityEnName,String regionEnName,String district);

    //地图找房
    ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName);

    //城市级别查询
    ServiceMultiResult<Integer> mapQuery(String cityEnName,String orderBy,String orderDirection,int start,int size);


    ServiceMultiResult<Integer> mapQuery(MapSearch mapSearch);
}
