package com.itchina;

import com.itchina.entity.House;
import com.itchina.service.search.constants.HouseIndexTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/28
 **/

@Service
public class Test implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

    }
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        ModelMapper modelMapper = new ModelMapper();
        House house = new House();
        house.setId(123);
        house.setTitle("dadad");
        HouseIndexTemplate map = modelMapper.map(house, HouseIndexTemplate.class);
        //System.out.println(map);
        HouseIndexTemplate HouseIndexTemplate = new HouseIndexTemplate();
        BeanUtils.copyProperties(house,HouseIndexTemplate);
        System.out.println(HouseIndexTemplate);

    }

}
