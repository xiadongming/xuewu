package com.itchina.service;

import com.itchina.dto.UserDTO;
import com.itchina.entity.User;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
public interface IUserService {


    User findUserByName(String userName);


    ServiceResult<UserDTO> findById(Integer adminId);

    User findUserByTelephone(String telephone);

    User addUserByPhone(String telephone);
}
