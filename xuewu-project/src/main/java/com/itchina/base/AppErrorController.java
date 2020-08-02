package com.itchina.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/16
 *  发生异常的时候，调用此controller,spring会执行ErrorController
 **/
@Controller
public class AppErrorController implements ErrorController {
    final Logger logger = LoggerFactory.getLogger(AppErrorController.class);
    private static final String error_path = "/error";

    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return error_path;
    }

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * 处理对应的页面即 text/html
     **/
    @RequestMapping(value = error_path, produces = "text/html")
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        int status2 = (int) request.getAttribute("javax.servlet.error.status_code");
        System.out.println("status2=  " + status2);
        logger.info("status=  " + status + ", url= " + request.getRequestURL());
        switch (status) {
            case 403:
                return "403";
            case 404:
                return "404";
            case 500:
                return "500";
        }
        return "index";
    }

    /**
     * 处理页面之外的api，如json/xml
     **/
    @RequestMapping(value = error_path, method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse errorApiHandler(HttpServletRequest request) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> attrMap = this.errorAttributes.getErrorAttributes(requestAttributes, false);
        int status = getStatus(request);
        return ApiResponse.ofMessage(status,String.valueOf(attrMap.getOrDefault("message","error")));
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (null != status) {
            return status;
        }
        return 500;
    }

}
