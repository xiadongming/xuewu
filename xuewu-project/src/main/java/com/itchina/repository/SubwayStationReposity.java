package com.itchina.repository;

import com.itchina.entity.SubwayStation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public interface SubwayStationReposity extends CrudRepository<SubwayStation,Integer> {
    List<SubwayStation> findBySubwayId(Integer subwayId);
}
