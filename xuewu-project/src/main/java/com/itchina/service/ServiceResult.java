package com.itchina.service;

import com.itchina.dto.HouseDTO;
import com.itchina.dto.SubwayDTO;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
public class ServiceResult<T> {

    private boolean success;
    private String message;
    private T result;

    public ServiceResult(boolean success) {
        this.success = success;
    }

    public ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ServiceResult(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public static <T> ServiceResult<T> of(T result) {

        return new ServiceResult(true, null,result);
    }

    public static <T> ServiceResult<T> notFound() {
        return new ServiceResult<T>(false,"没有数据");
    }

    public static <T> ServiceResult success() {
        return new ServiceResult<T>(true,"操作成功");
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ServiceResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
