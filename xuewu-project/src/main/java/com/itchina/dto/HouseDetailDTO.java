package com.itchina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/

public class HouseDetailDTO {
    private Integer id;
    private String description;

    private String layoutDesc;

    private String traffic;

    private String roundService;

    private Integer rentWay;

    private String address;

    private Integer subwayLineId;

    private String subwayLineName;

    private Integer subwayStationId;

    private String subwayStationName;

    private Integer houseId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLayoutDesc(String layoutDesc) {
        this.layoutDesc = layoutDesc;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public void setRoundService(String roundService) {
        this.roundService = roundService;
    }

    public void setRentWay(Integer rentWay) {
        this.rentWay = rentWay;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSubwayLineId(Integer subwayLineId) {
        this.subwayLineId = subwayLineId;
    }

    public void setSubwayLineName(String subwayLineName) {
        this.subwayLineName = subwayLineName;
    }

    public void setSubwayStationId(Integer subwayStationId) {
        this.subwayStationId = subwayStationId;
    }

    public void setSubwayStationName(String subwayStationName) {
        this.subwayStationName = subwayStationName;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getLayoutDesc() {
        return layoutDesc;
    }

    public String getTraffic() {
        return traffic;
    }

    public String getRoundService() {
        return roundService;
    }

    public Integer getRentWay() {
        return rentWay;
    }

    public String getAddress() {
        return address;
    }

    public Integer getSubwayLineId() {
        return subwayLineId;
    }

    public String getSubwayLineName() {
        return subwayLineName;
    }

    public Integer getSubwayStationId() {
        return subwayStationId;
    }

    public String getSubwayStationName() {
        return subwayStationName;
    }

    public Integer getHouseId() {
        return houseId;
    }

    @Override
    public String toString() {
        return "HouseDetail{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", layoutDesc='" + layoutDesc + '\'' +
                ", traffic='" + traffic + '\'' +
                ", roundService='" + roundService + '\'' +
                ", rentWay=" + rentWay +
                ", address='" + address + '\'' +
                ", subwayLineId=" + subwayLineId +
                ", subwayLineName='" + subwayLineName + '\'' +
                ", subwayStationId=" + subwayStationId +
                ", subwayStationName='" + subwayStationName + '\'' +
                ", houseId=" + houseId +
                '}';
    }
}
