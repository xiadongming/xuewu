package com.itchina.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/***
 *  @auther xiadongming
 *  @date 2020/7/16
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源加载
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    }

    /**
     * 模板资源解析
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.thymeleaf")//自定义thy需要配置此处
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        return templateResolver;
    }

    /***
     * thymeleaf标准方言解析器
     * **/
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        //支持spring的el表达式
        templateEngine.setEnableSpringELCompiler(true);
        //支持springsecurity方言
        SpringSecurityDialect securityDialect = new SpringSecurityDialect();
        templateEngine.addDialect(securityDialect);
        return templateEngine;
    }

    /**
     * 试图解析器
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    /**
     * 属性赋值，类似spring的BeanUtils.copyProperties()功能
     * **/
    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }

}
