package com.itchina.entity;

import javax.persistence.*;
import java.util.Date;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
@Entity
@Table(name = "house")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer price;
    private Integer area;
    private Integer room;
    private Integer floor;

    @Column(name = "total_floor")
    private Integer totalFloor;

    @Column(name = "watch_times")
    private Integer watchTimes;

    @Column(name = "build_year")
    private Integer buildYear;

    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @Column(name = "city_en_name")
    private String cityEnName;

    @Column(name = "region_en_name")
    private String regionEnName;

    private String cover;
    private Integer direction;

    @Column(name = "distance_to_subway")
    private Integer distanceToSubway;

    private Integer parlour;
    private String district;

    @Column(name = "admin_id")
    private Integer adminId;

    private Integer bathroom;
    private String street;


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
