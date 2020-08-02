package com.itchina.service.impl;

import com.itchina.dto.SupportAddressDTO;
import com.itchina.entity.SupportAddress;
import com.itchina.repository.SupportAddressReposity;
import com.itchina.security.AuthProvider;
import com.itchina.service.IAddressService;
import com.itchina.service.ServiceMultiResult;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
@Service
public class AddressServiceImpl implements IAddressService {

    final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private SupportAddressReposity supportAddressReposity;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        logger.info("Level.CITY.getValue()=  " + SupportAddress.Level.CITY.getValue());
        List<SupportAddress> allSupportAddress = supportAddressReposity.findAllByLevel(SupportAddress.Level.CITY.getValue());
        List<SupportAddressDTO> supportAddressDTOList = new ArrayList<>();
        //同BeanUtils.copyProperties()功能
        for (SupportAddress supportAddress : allSupportAddress) {
            SupportAddressDTO supportAddressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);
            supportAddressDTOList.add(supportAddressDTO);
        }
        return new ServiceMultiResult<>(supportAddressDTOList.size(),supportAddressDTOList);
    }
}
