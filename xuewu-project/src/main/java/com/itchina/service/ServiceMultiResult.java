package com.itchina.service;

import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
public class ServiceMultiResult<T> {

    private long total;

    private List<T> result;

    public ServiceMultiResult(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getResult() {
        return result;
    }

    public int getResultSize() {
        if (null == this.result) {
            return 0;
        }
        return this.result.size();
    }
}
