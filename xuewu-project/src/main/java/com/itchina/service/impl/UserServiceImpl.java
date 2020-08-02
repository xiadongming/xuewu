package com.itchina.service.impl;

import com.itchina.entity.Role;
import com.itchina.entity.User;
import com.itchina.repository.RoleRepository;
import com.itchina.repository.UserRepository;
import com.itchina.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findUserByName(String userName) {
        User user = userRepository.findByName(userName);
        if (null == user) {
            return null;
        }
        List<Role> rolesList = roleRepository.findRolesByUserId(user.getId());
        if (null == rolesList || rolesList.size() == 0) {
            throw new DisabledException("权限非法");
        }
        //权限赋值
        ArrayList<GrantedAuthority> authoritiesList = new ArrayList<>();
        for (Role role : rolesList) {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        user.setAuthorityList(authoritiesList);
        return user;
    }
}
