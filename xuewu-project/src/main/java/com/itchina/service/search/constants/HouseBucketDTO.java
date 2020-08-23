package com.itchina.service.search.constants;

/**
 * @Author: xiadongming
 * @Date: 2020/8/23 10:41
 */
public class HouseBucketDTO {


    private String key;

    private long count;

    public HouseBucketDTO(String key, long count) {
        this.key = key;
        this.count = count;
    }

    public HouseBucketDTO() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public long getCount() {
        return count;
    }
}
