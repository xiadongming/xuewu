package com.itchina.web.controller.admin;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.itchina.base.ApiResponse;
import com.itchina.common.Constants;
import com.itchina.fastdfsutils.FastDfsService;
import com.itchina.security.LoginAuthFailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@Controller
public class AdminController {

    final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private FastDfsService fastDfsService;


    @RequestMapping(value = "/admin/center", method = RequestMethod.GET)
    public String adminCenter() {
        return "admin/center";
    }

    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public String adminLogin() {
        return "admin/login";
    }

    @RequestMapping(value = "/admin/welcome", method = RequestMethod.GET)
    public String welcomePage() {
        return "admin/welcome";
    }

    @RequestMapping(value = "/admin/add/house", method = RequestMethod.GET)
    public String addHousePage() {
        return "admin/house-add";
    }

    /**
     * 图片上传接口
     **/
    @RequestMapping(value = "admin/upload/photo", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhotoFastdfs(@RequestParam("file") MultipartFile file) {
        if (null == file) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        StorePath storePath = null;
        try {
            storePath = fastDfsService.upload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = storePath.getPath();
        logger.info("path=  " + path);

        String photoFullPath = Constants.http_path + path;

        String filename = file.getOriginalFilename();
        logger.info("filename= " + filename);
        File target = new File("D:\\z_test_pic\\" + filename);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 图片上传接口
     * 暂时不用，而是上传到fastdfs，不是本地服务器
     **/
    @RequestMapping(value = "admin/upload/photo2", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhotoNative(@RequestParam("file") MultipartFile file) {
        if (null == file) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        String filename = file.getOriginalFilename();
        logger.info("filename= " + filename);
        File target = new File("D:\\z_test_pic\\" + filename);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.ofSuccess(null);
    }
}
