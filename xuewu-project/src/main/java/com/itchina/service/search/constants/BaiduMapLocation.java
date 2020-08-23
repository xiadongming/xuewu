package com.itchina.service.search.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author: xiadongming
 * @Date: 2020/8/23 13:16
 */
public class BaiduMapLocation {

    //经度
    @JsonProperty("lon")
    private double longitude;

    //维度
    @JsonProperty("lat")
    private double latitude;

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
