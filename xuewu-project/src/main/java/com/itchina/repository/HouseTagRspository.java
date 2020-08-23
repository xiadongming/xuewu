package com.itchina.repository;

import com.itchina.entity.HouseTag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public interface HouseTagRspository extends CrudRepository<HouseTag,Integer> {
    List<HouseTag> findAllByHouseId(Integer intId);

    List<HouseTag> findAllByHouseIdIn(List<Integer> ids);
}
