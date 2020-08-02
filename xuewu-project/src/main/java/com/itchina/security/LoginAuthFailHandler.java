package com.itchina.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *  @auther xiadongming
 *  @date 2020/7/18
 *  验证失败处理器，即输入用户名/密码失败的时候，会调用此controller、
 **/
public class LoginAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    final Logger logger = LoggerFactory.getLogger(LoginAuthFailHandler.class);

    private final LoginUrlEntryPoint urlEntryPoint;

    public LoginAuthFailHandler(LoginUrlEntryPoint urlEntryPoint) {
        this.urlEntryPoint = urlEntryPoint;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = this.urlEntryPoint.determineUrlToUseForThisRequest(request, response, exception);
        targetUrl += "?" + exception.getMessage();
        logger.info("targetUrl= " + targetUrl);

        super.setDefaultFailureUrl(targetUrl);
        super.onAuthenticationFailure(request, response, exception);

    }
}
