package com.itchina.service.impl;

import com.google.common.collect.Lists;
import com.itchina.dto.UserDTO;
import com.itchina.entity.Role;
import com.itchina.entity.User;
import com.itchina.repository.RoleRepository;
import com.itchina.repository.UserRepository;
import com.itchina.service.IUserService;
import com.itchina.service.ServiceResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ModelMapper modelMapper;


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

    @Override
    public ServiceResult<UserDTO> findById(Integer userId) {

        User user = userRepository.findOne(Long.parseLong(String.valueOf(userId)));
        if (user == null) {
            return ServiceResult.notFound();
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ServiceResult.of(userDTO);
    }

    @Override
    public User findUserByTelephone(String telephone) {
        User user = userRepository.findUserByPhoneNumber(telephone);
        if (user == null) {
            return null;
        }
        List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        if (roles == null || roles.isEmpty()) {
            throw new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }

    @Override
    @Transactional
    public User addUserByPhone(String telephone) {
        User user = new User();
        user.setPhoneNumber(telephone);
        user.setName(telephone.substring(0, 3) + "****" + telephone.substring(7, telephone.length()));
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setLastUpdateTime(now);
        user = userRepository.save(user);

        Role role = new Role();
        role.setName("USER");
        role.setUserId(user.getId());
        roleRepository.save(role);
        user.setAuthorityList(Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER")));
        return user;
    }

}
