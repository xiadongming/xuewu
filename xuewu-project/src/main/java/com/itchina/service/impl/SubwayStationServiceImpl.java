package com.itchina.service.impl;

import com.itchina.dto.SubwayStationDTO;
import com.itchina.entity.SubwayStation;
import com.itchina.repository.SubwayStationReposity;
import com.itchina.service.ISubwayStationService;
import com.itchina.service.ServiceMultiResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
@Service
public class SubwayStationServiceImpl implements ISubwayStationService {
    @Autowired
    private SubwayStationReposity subwayStationReposity;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ServiceMultiResult<SubwayStationDTO> findBySubwayId(String subwayId) {

        List<SubwayStation> subwayStationList = subwayStationReposity.findBySubwayId(Integer.valueOf(subwayId));
        SubwayStationDTO subwayStationDTO = null;
        List<SubwayStationDTO> subwayStationDTOS = new ArrayList<>();
        for (SubwayStation subwayStation : subwayStationList) {
            subwayStationDTO = modelMapper.map(subwayStation, SubwayStationDTO.class);
            subwayStationDTOS.add(subwayStationDTO);
        }
        return new ServiceMultiResult<>(subwayStationList.size(),subwayStationDTOS);
    }

    @Override
    public SubwayStation findOne(Long subwayStationId) {
        SubwayStation subwayStation = subwayStationReposity.findOne(Integer.valueOf(subwayStationId.toString()));
        return subwayStation;
    }
}
