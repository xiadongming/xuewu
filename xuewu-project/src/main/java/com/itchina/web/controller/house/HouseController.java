package com.itchina.web.controller.house;

import com.itchina.base.ApiResponse;
import com.itchina.dto.SupportAddressDTO;
import com.itchina.service.IAddressService;
import com.itchina.service.ServiceMultiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
@Controller
public class HouseController {
    @Autowired
    private IAddressService addressService;

    @RequestMapping(value = "address/support/cities", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getSupportCities() {
        ServiceMultiResult<SupportAddressDTO> allCities = addressService.findAllCities();
        if (null == allCities || allCities.getResultSize() == 0) {
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }
           return  ApiResponse.ofSuccess(allCities.getResult());
    }




    public static void main(String[] args) {
        List<SupportAddressDTO> strList = new ArrayList<>();
        SupportAddressDTO SupportAddressDTO1 = new SupportAddressDTO();
        SupportAddressDTO1.setBelongTo("1");
        SupportAddressDTO SupportAddressDTO2 = new SupportAddressDTO();
        SupportAddressDTO2.setBelongTo("2");
        SupportAddressDTO SupportAddressDTO3 = new SupportAddressDTO();
        SupportAddressDTO3.setBelongTo("3");
        strList.add(SupportAddressDTO1);
        strList.add(SupportAddressDTO2);
        strList.add(SupportAddressDTO3);

        for (SupportAddressDTO supportAddressDTO : strList) {
            if(supportAddressDTO.getBelongTo().equals("3")){
                supportAddressDTO.setCnName("北京北京北京");
            }
        }

        System.out.println(strList);
    }

}
