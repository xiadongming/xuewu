package com.itchina.web.controller;

import com.itchina.base.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 *  @auther xiadongming
 *  @date 2020/7/16
 **/
@Controller
public class HomeController {
    @RequestMapping("/")
    public String getTest(Model model) {
        model.addAttribute("name", "缺的东西很多，缺实战，缺经验，找工作不难，呆下去才难");
        return "index";
    }

    @RequestMapping(value = "/response", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getResponse() {
        return ApiResponse.ofMessage(200, "成功了");
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String notFound() {
        return "404";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessError() {
        return "403";
    }

    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public String internalError() {
        return "500";
    }
    //logout是springboot官方的登出页面，此处设置为自定义的logout/page
    @RequestMapping(value = "/logout/page", method = RequestMethod.GET)
    public String logoutPage() {
        return "logout";
    }
}
