package com.itchina.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 *  基于角色的登录入口控制，即不同的角色，登录入口不同，即  user对应user的登录页面，admin对应admin的登录页面
 **/
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    final Logger logger = LoggerFactory.getLogger(LoginUrlEntryPoint.class);

    private PathMatcher pathMatcher = new AntPathMatcher();

    private final Map<String, String> authEntryPointMap;

    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        authEntryPointMap = new HashMap<>();
        authEntryPointMap.put("/user/**", "/user/login");
        authEntryPointMap.put("/admin/**", "/admin/login");
        /**
         * 以"/user/**"开头的请求，都会跳转到"/usr/login"上
         * 以"/admin/**"开头的请求，都会跳转到"/admin/login"上
         * **/
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        logger.info("uri= "+request.getRequestURI()+",replace= "+uri);
        logger.info("url= "+request.getRequestURL());
        logger.info("contextPath= "+request.getContextPath());

        for (Map.Entry<String, String> authEntry : this.authEntryPointMap.entrySet()) {
            if(this.pathMatcher.match(authEntry.getKey(),uri)){
                return  authEntry.getValue();
            }
        }
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
