package com.itchina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/

public class HouseDTO {
    private Integer id;
    private String title;
    private Integer price;
    private Integer area;
    private Integer room;
    private Integer floor;

    @JsonProperty(value= "total_floor")
    private Integer totalFloor;

    @JsonProperty(value = "watch_times")
    private Integer watchTimes;

    @JsonProperty(value = "build_year")
    private Integer buildYear;

    private Integer status;

    @JsonProperty(value = "create_time")
    private Date createTime;

    @JsonProperty(value = "last_update_time")
    private Date lastUpdateTime;

    @JsonProperty(value = "city_en_name")
    private String cityEnName;

    @JsonProperty(value = "region_en_name")
    private String regionEnName;

    private String cover;
    private Integer direction;

    @JsonProperty(value = "distance_to_subway")
    private Integer distanceToSubway;

    private Integer parlour;
    private String district;

    @JsonProperty(value = "admin_id")
    private Integer adminId;

    private Integer bathroom;
    private String street;


    private HouseDetailDTO houseDetail;

    private List<String> tags;

    private List<HousePictureDTO> pictures;

    private int subscribeStatus;

    public void setDistanceToSubway(int distanceToSubway) {
        this.distanceToSubway = distanceToSubway;
    }

    public void setHouseDetail(HouseDetailDTO houseDetail) {
        this.houseDetail = houseDetail;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setPictures(List<HousePictureDTO> pictures) {
        this.pictures = pictures;
    }

    public void setSubscribeStatus(int subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public HouseDetailDTO getHouseDetail() {
        return houseDetail;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<HousePictureDTO> getPictures() {
        return pictures;
    }

    public int getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public void setWatchTimes(Integer watchTimes) {
        this.watchTimes = watchTimes;
    }

    public void setBuildYear(Integer buildYear) {
        this.buildYear = buildYear;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public void setRegionEnName(String regionEnName) {
        this.regionEnName = regionEnName;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setDistanceToSubway(Integer distanceToSubway) {
        this.distanceToSubway = distanceToSubway;
    }

    public void setParlour(Integer parlour) {
        this.parlour = parlour;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setBathroom(Integer bathroom) {
        this.bathroom = bathroom;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getArea() {
        return area;
    }

    public Integer getRoom() {
        return room;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public Integer getWatchTimes() {
        return watchTimes;
    }

    public Integer getBuildYear() {
        return buildYear;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public String getRegionEnName() {
        return regionEnName;
    }

    public String getCover() {
        return cover;
    }

    public Integer getDirection() {
        return direction;
    }

    public Integer getDistanceToSubway() {
        return distanceToSubway;
    }

    public Integer getParlour() {
        return parlour;
    }

    public String getDistrict() {
        return district;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public Integer getBathroom() {
        return bathroom;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", room=" + room +
                ", floor=" + floor +
                ", totalFloor=" + totalFloor +
                ", watchTimes=" + watchTimes +
                ", buildYear=" + buildYear +
                ", status=" + status +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", cityEnName='" + cityEnName + '\'' +
                ", regionEnName='" + regionEnName + '\'' +
                ", cover='" + cover + '\'' +
                ", direction=" + direction +
                ", distanceToSubway=" + distanceToSubway +
                ", parlour=" + parlour +
                ", district='" + district + '\'' +
                ", adminId=" + adminId +
                ", bathroom=" + bathroom +
                ", street='" + street + '\'' +
                '}';
    }
}
