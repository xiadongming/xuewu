package com.itchina.service;

import com.itchina.dto.SubwayDTO;
import com.itchina.dto.SupportAddressDTO;
import com.itchina.entity.Subway;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public interface ISubwayService {

    ServiceMultiResult<SubwayDTO> findAllSunwayByCityEnName(String cityEnName);

    Subway findOne(Long subwayLineId);
}
