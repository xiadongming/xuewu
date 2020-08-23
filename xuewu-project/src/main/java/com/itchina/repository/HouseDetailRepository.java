package com.itchina.repository;

import com.itchina.entity.HouseDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public interface HouseDetailRepository extends CrudRepository<HouseDetail,Integer> {
    HouseDetail findByHouseId(Integer intId);

    List<HouseDetail> findAllByHouseIdIn(List<Integer> ids);
}
