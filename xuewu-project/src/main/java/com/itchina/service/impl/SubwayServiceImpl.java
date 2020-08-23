package com.itchina.service.impl;

import com.itchina.dto.SubwayDTO;
import com.itchina.entity.Subway;
import com.itchina.repository.SubwayReposity;
import com.itchina.service.ISubwayService;
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
public class SubwayServiceImpl implements ISubwayService {

    @Autowired
    private SubwayReposity subwayReposity;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ServiceMultiResult<SubwayDTO> findAllSunwayByCityEnName(String cityEnName) {
        List<Subway> lineList =  subwayReposity.findByCityEnName(cityEnName);
        List<SubwayDTO> subwayDTOList = new ArrayList<>();
        SubwayDTO subwayDTO = null;
        for (Subway subway : lineList) {
            subwayDTO = modelMapper.map(subway, SubwayDTO.class);
            subwayDTOList.add(subwayDTO);
        }
        return new ServiceMultiResult(subwayDTOList.size(),subwayDTOList);
    }

    @Override
    public Subway findOne(Long subwayLineId) {
        Subway subWay = subwayReposity.findOne(Integer.valueOf(String.valueOf(subwayLineId)));
        return subWay;
    }
}
