package com.itchina.repository;

import com.itchina.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findRolesByUserId(Long userId);


}
