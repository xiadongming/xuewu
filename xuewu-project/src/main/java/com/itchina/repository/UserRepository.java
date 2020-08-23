package com.itchina.repository;

import com.itchina.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * JPA的命名方式，是有固定格式的,jpa会自动生成sql语句
     **/
    User findByName(String userName);


    User findUserByPhoneNumber(String telephone);
}
