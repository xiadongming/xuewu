package com.itchina.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author: xiadongming
 * @Date: 2020/8/10 22:21
 * session共享
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)//秒，即一天
public class RedisSessionConfig {

    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){
      return new StringRedisTemplate(factory);
    }

}
