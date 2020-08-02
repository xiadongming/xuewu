package com.itchina.config;

import com.itchina.security.AuthProvider;
import com.itchina.security.LoginAuthFailHandler;
import com.itchina.security.LoginUrlEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * http权限控制
     * 资源管理权限
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /***
         * 规律：
         *除了放行的即permitAll()的url，其他的都要走 loginProcessingUrl("/login")//登录入口
         * **/
        http.authorizeRequests()
                .antMatchers("/admin/login").permitAll() //管理员登录入口
                .antMatchers("/user/login").permitAll()  //用户登录入口
                .antMatchers("/static/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .loginProcessingUrl("/login").defaultSuccessUrl("/")//登录入口
                //.loginProcessingUrl("/login")//登录入口
                .failureHandler(authFailHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/page")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint())
                .accessDeniedPage("/403");
        //关闭csrf防御策略
        http.csrf().disable();
        //设置同源策略
        http.headers().frameOptions().sameOrigin();
    }

    /**
     * 配置一个默认的登录用户
     **/
    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN").and();
         auth.authenticationProvider(authProvider()).eraseCredentials(true);

    }

    @Bean
    public AuthProvider authProvider() {
        return new AuthProvider();
    }

    /**
     * 判断是admin，还是user用户登录
     * **/
    @Bean
    public LoginUrlEntryPoint urlEntryPoint(){
        return new LoginUrlEntryPoint("/user/login");
    }

    /**
     * 验证错误处理器
     * **/
    @Bean
    public LoginAuthFailHandler authFailHandler(){
        return  new LoginAuthFailHandler(urlEntryPoint());
    }

}
