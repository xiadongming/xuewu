package com.itchina.web.controller.admin;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.itchina.base.ApiDataTableResponse;
import com.itchina.base.ApiResponse;
import com.itchina.base.HouseOperation;
import com.itchina.base.HouseStatus;
import com.itchina.common.Constants;
import com.itchina.dto.*;
import com.itchina.entity.SupportAddress;
import com.itchina.fastdfsutils.FastDfsService;
import com.itchina.form.DatatableSearch;
import com.itchina.form.HouseForm;
import com.itchina.security.LoginAuthFailHandler;
import com.itchina.service.IAddressService;
import com.itchina.service.IHouseService;
import com.itchina.service.ServiceMultiResult;
import com.itchina.service.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.security.x509.CertAttrSet;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/***
 *  @auther xiadongming
 *  @date 2020/7/17
 **/
@Controller
public class AdminController {

    final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private FastDfsService fastDfsService;
    @Autowired
    private IHouseService houseService;
    @Autowired
    private IAddressService addressService;


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

    @RequestMapping(value = "/admin/house/list", method = RequestMethod.GET)
    public String addHouseList() {
        return "admin/house-list";
    }
/*
    @RequestMapping(value = "/admin/house/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiDataTableResponse addHouseList(DatatableSearch searchBody) {
        ServiceMultiResult<HouseDTO> result = houseService.admingQWuery(searchBody);

        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());
        response.setDraw(searchBody.getDraw());
        return response;
    }
*/

    @RequestMapping(value = "/admin/houses", method = RequestMethod.POST)
    @ResponseBody                     //@ModelAttribute
    public ApiDataTableResponse houses(DatatableSearch searchBody) {
        ServiceMultiResult<HouseDTO> result = houseService.admingQWuery(searchBody);

        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());
        response.setDraw(searchBody.getDraw());
        return response;
    }

    @RequestMapping(value = "/admin/house/subscribe", method = RequestMethod.GET)
    public String addHouseSubscribe() {
        return "admin/subscribe";
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

    @RequestMapping(value = "/admin/add/house/form", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ApiResponse addHouse(HouseForm houseForm, BindingResult bindingResult) {
        logger.info("入参：houseForm= " + houseForm);
        if (bindingResult.hasErrors()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
        }
       /* if(null == houseForm.getPhotos() || null == houseForm.getCover()){
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(),"图品或者封面为空");
        }*/
        Map<SupportAddress.Level, SupportAddressDTO> addResddMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());
        if (CollectionUtils.isEmpty(addResddMap) || addResddMap.size() != 2) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        ServiceResult<HouseDTO> saveResult = houseService.save(houseForm);

        if (saveResult.isSuccess()) {
            return ApiResponse.ofSuccess(saveResult);
        }
        return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAM);
    }

    //编辑页面
    @RequestMapping(value = "/admin/house/edit", method = RequestMethod.GET)
    public String houseEditPage(@RequestParam(value = "id") Long id, Model model) {
        if (null == id || id < 1) {
            return "404";
        }
        ServiceResult<HouseDTO> completeOne = houseService.findCompleteOne(id);
        if (null == completeOne) {
            return "404";
        }
        HouseDTO result = completeOne.getResult();
        model.addAttribute("house", result);
        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(result.getCityEnName(), result.getRegionEnName());
        model.addAttribute("city", addressMap.get(SupportAddress.Level.CITY));
        model.addAttribute("region", addressMap.get(SupportAddress.Level.REGION));
        HouseDetailDTO houseDetail = result.getHouseDetail();
        ServiceResult<SubwayDTO> subwayDTOServiceResult = addressService.findSubway(houseDetail.getSubwayLineId());

        if (subwayDTOServiceResult.isSuccess()) {
            model.addAttribute("subway", subwayDTOServiceResult.getResult());
        }
        ServiceResult<SubwayStationDTO> subwayStationDTOServiceResult = addressService.findSubwayStation(houseDetail.getSubwayStationId());

        if (subwayStationDTOServiceResult.isSuccess()) {
            model.addAttribute("station", subwayStationDTOServiceResult.getResult());
        }
        return "admin/house-edit";
    }

    //编辑页面，更新操作
    @RequestMapping(value = "/admin/house/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse saveHouse(@ModelAttribute("form-house-edit") HouseForm houseForm, BindingResult bindingResult) {
        ServiceResult<HouseDTO> result = houseService.update(houseForm);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessge("操作成功");
        return apiResponse;
    }

    /**
     * 审核接口：发布，删除等
     **/

    @RequestMapping(value = "/admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operateHouse(@PathVariable(value = "id") Long id, @PathVariable(value = "operation") int operation) throws Exception {
        if (id <= 0) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        ServiceResult serviceResult = null;
        switch (operation) {
            case HouseOperation.PASS:
                serviceResult = this.houseService.updateStatus(id, HouseStatus.PASSES.getValue());
                break;
            case HouseOperation.PULL_OUT:
                serviceResult = this.houseService.updateStatus(id, HouseStatus.NOT_AUTITED.getValue());
                break;
            case HouseOperation.DELETE:
                serviceResult = this.houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HouseOperation.RENT:
                serviceResult = this.houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
        if (serviceResult.isSuccess()) {
            return ApiResponse.ofSuccess(null);
        }
        return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), serviceResult.getMessage());
    }


}
