package com.itchina.repository;

import com.itchina.entity.Subway;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public interface SubwayReposity extends CrudRepository<Subway,Integer> {


    List<Subway> findByCityEnName(String cityEnName);
}
