package com.itchina.service.search.constants;

import java.util.Date;
import java.util.List;

/**
 * @Author: xiadongming
 * @Date: 2020/8/16 22:49
 * 和xuewu索引，house类型字段一一对应
 */
public class HouseIndexTemplate {

    private Integer houseId;

    private String title; //String类型和es的text类型对应（需要分词和text对应。不需要分词和keyword对应）

    private int price;

    private int area;

    private Date createTime;

    private Date lastUpdateTime;

    private String cityEnName;

    private String regionEnName;

    private int direction;

    private int distanceToSubway;

    private String subwayLineName;

    private String subwayStationName;

    private String street;

    private String district;

    private String description;

    private String layoutDesc;

    private String traffic;

    private String roundService;

    private int rentWay;

    private List<String> tags;

    //自动补全字段
    private List<HouseSuggest> suggest;

    private BaiduMapLocation baiduMapLocation;

    public void setBaiduMapLocation(BaiduMapLocation baiduMapLocation) {
        this.baiduMapLocation = baiduMapLocation;
    }

    public BaiduMapLocation getBaiduMapLocation() {
        return baiduMapLocation;
    }

    public void setSuggest(List<HouseSuggest> suggest) {
        this.suggest = suggest;
    }

    public List<HouseSuggest> getSuggest() {
        return suggest;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setArea(int area) {
        this.area = area;
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

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setDistanceToSubway(int distanceToSubway) {
        this.distanceToSubway = distanceToSubway;
    }

    public void setSubwayLineName(String subwayLineName) {
        this.subwayLineName = subwayLineName;
    }

    public void setSubwayStationName(String subwayStationName) {
        this.subwayStationName = subwayStationName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public void setRentWay(int rentWay) {
        this.rentWay = rentWay;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getArea() {
        return area;
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

    public int getDirection() {
        return direction;
    }

    public int getDistanceToSubway() {
        return distanceToSubway;
    }

    public String getSubwayLineName() {
        return subwayLineName;
    }

    public String getSubwayStationName() {
        return subwayStationName;
    }

    public String getStreet() {
        return street;
    }

    public String getDistrict() {
        return district;
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

    public int getRentWay() {
        return rentWay;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "HouseIndexTemplate{" +
                "houseId=" + houseId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", cityEnName='" + cityEnName + '\'' +
                ", regionEnName='" + regionEnName + '\'' +
                ", direction=" + direction +
                ", distanceToSubway=" + distanceToSubway +
                ", subwayLineName='" + subwayLineName + '\'' +
                ", subwayStationName='" + subwayStationName + '\'' +
                ", street='" + street + '\'' +
                ", district='" + district + '\'' +
                ", description='" + description + '\'' +
                ", layoutDesc='" + layoutDesc + '\'' +
                ", traffic='" + traffic + '\'' +
                ", roundService='" + roundService + '\'' +
                ", rentWay=" + rentWay +
                ", tags=" + tags +
                ", suggest=" + suggest +
                ", baiduMapLocation=" + baiduMapLocation +
                '}';
    }
}
