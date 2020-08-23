package com.itchina.service.sms.impl;

import com.itchina.service.ServiceResult;
import com.itchina.service.sms.ISmsService;
import org.springframework.stereotype.Service;

/**
 * @Author: xiadongming
 * @Date: 2020/8/23 16:25
 */
@Service
public class SmsServiceImpl implements ISmsService {
    @Override
    public ServiceResult<String> sendSms(String telephone) {
        return ServiceResult.of("123456");
    }

    @Override
    public String getSmsCode(String telehone) {
        return "123456";
    }

    @Override
    public void remove(String telephone) {

    }
}
