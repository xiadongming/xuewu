package com.itchina.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@Controller
public class UserController {

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String loginPage() {
        return "user/login";
    }

    @RequestMapping(value = "/user/center", method = RequestMethod.GET)
    public String centerPage() {
        return "user/center";
    }


}
