package com.itchina.repository;

import com.itchina.entity.HousePicture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public interface HousePictureRepository extends CrudRepository<HousePicture,Integer> {
    List<HousePicture> findAllByHouseId(Integer intId);
}
