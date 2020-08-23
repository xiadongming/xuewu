package com.itchina.service;

import com.itchina.dto.SubwayDTO;
import com.itchina.dto.SubwayStationDTO;
import com.itchina.entity.SubwayStation;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public interface ISubwayStationService {

    ServiceMultiResult<SubwayStationDTO> findBySubwayId(String subwayId);

    SubwayStation findOne(Long subwayStationId);
}
