package com.itchina.service;

import com.itchina.dto.SubwayDTO;
import com.itchina.dto.SubwayStationDTO;
import com.itchina.dto.SupportAddressDTO;
import com.itchina.entity.SupportAddress;
import com.itchina.service.search.constants.BaiduMapLocation;

import java.util.List;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
public interface IAddressService {

    ServiceMultiResult<SupportAddressDTO> findAllCities();

    ServiceMultiResult<SupportAddressDTO> findRegionsByCityName(String cityName);

    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    ServiceResult<SubwayDTO> findSubway(Integer subwayLineId);

    ServiceResult<SubwayStationDTO> findSubwayStation(Integer subwayStationId);

    ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityEnName);

    ServiceResult<SupportAddressDTO> findCity(String cityEnName);

   ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city,String address);


}
