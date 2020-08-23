package com.itchina.security;

import com.itchina.base.LoginUserUtils;
import com.itchina.entity.User;
import com.itchina.service.IUserService;
import com.itchina.service.sms.ISmsService;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author: xiadongming
 * @Date: 2020/8/23 16:27
 */
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISmsService smsService;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String name = obtainUsername(request);
        if (!Strings.isNullOrEmpty(name)) {
            request.setAttribute("username", name);
            return super.attemptAuthentication(request, response);
        }
        String telephone = request.getParameter("telephone");
        if (Strings.isNullOrEmpty(telephone) || !LoginUserUtils.checkTelephone(telephone)) {
            throw new BadCredentialsException("Wrong telephone number");
        }
        User user = userService.findUserByTelephone(telephone);
        //获取验证码
        String inputCode = request.getParameter("smsCode");
        String sessionCode = smsService.getSmsCode(telephone);
        if (Objects.equals(inputCode, sessionCode)) {
            if (user == null) { // 如果用户第一次用手机登录 则自动注册该用户
                user = userService.addUserByPhone(telephone);
            }
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            throw new BadCredentialsException("smsCodeError");
        }
    }
}
