package com.itchina.repository;

import com.itchina.entity.House;
import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
//public interface HouseRepository  extends CrudRepository<House,Integer> {
    //分页接口
public interface HouseRepository  extends PagingAndSortingRepository<House,Integer>, JpaSpecificationExecutor<House> {

    //JpaSpecificationExecutor功能是指定多个字段进行查询

    @Modifying
    @Query("update House as house set house.status = :status where house.id = :id ")
    void updateStatus(@Param(value="id") Integer id,@Param(value = "status") int status);
}
