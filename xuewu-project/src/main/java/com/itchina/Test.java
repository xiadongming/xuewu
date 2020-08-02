package com.itchina;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/***
 *  @auther xiadongming
 *  @date 2020/7/28
 **/

@Service
public class Test implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("  ==  ");
        throw new RuntimeException("");
    }
}
