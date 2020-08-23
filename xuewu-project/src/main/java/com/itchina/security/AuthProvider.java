package com.itchina.security;


import com.itchina.base.AppErrorController;
import com.itchina.entity.User;
import com.itchina.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 *  自定义认证策略
 **/
public class AuthProvider implements AuthenticationProvider {

    final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @Autowired
    private IUserService userService;

    private final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /**
         * 输入的用户名和密码
         * **/
        String name = authentication.getName();
        String passWord = (String) authentication.getCredentials();
        logger.info("用户输入的，，name= "+name+", passWord= "+passWord);
        User user = userService.findUserByName(name);
        logger.info("数据库查询出的用户信息：user= " + user);
        if (null == user) {
            throw new AuthenticationCredentialsNotFoundException("权限验证失败");
        }
        /***
         * 此处的api，需要搞明白，有些不懂
         * **/
        if (passwordEncoder.isPasswordValid(user.getPassword(), passWord, user.getId())) {
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        throw new BadCredentialsException("authError");//密码校验错误
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
