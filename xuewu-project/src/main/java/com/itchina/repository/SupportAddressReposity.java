package com.itchina.repository;

import com.itchina.entity.SupportAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
public interface SupportAddressReposity extends CrudRepository<SupportAddress,Long> {

    /**
     * 根据行政级别，获取对应的所有地址信息
     * */
    List<SupportAddress> findAllByLevel(String level);
}
