package com.itchina.service;

import com.itchina.dto.SupportAddressDTO;
import com.itchina.entity.SupportAddress;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
public interface IAddressService {

    ServiceMultiResult<SupportAddressDTO> findAllCities();

}
