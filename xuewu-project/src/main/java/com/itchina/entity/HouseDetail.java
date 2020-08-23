package com.itchina.entity;

import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
@Entity
@Table(name = "house_detail")
public class HouseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    @Column(name = "layout_desc")
    private String layoutDesc;

    private String traffic;

    @Column(name = "round_service")
    private String roundService;

    @Column(name = "rent_way")
    private Integer rentWay;

    @Column(name = "address")
    private String detailAddress;

    @Column(name = "subway_line_id")
    private Integer subwayLineId;

    @Column(name = "subway_line_name")
    private String subwayLineName;

    @Column(name = "subway_station_id")
    private Integer subwayStationId;

    @Column(name = "subway_station_name")
    private String subwayStationName;

    @Column(name = "house_id")
    private Integer houseId;

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

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
                ", detailAddress='" + detailAddress + '\'' +
                ", subwayLineId=" + subwayLineId +
                ", subwayLineName='" + subwayLineName + '\'' +
                ", subwayStationId=" + subwayStationId +
                ", subwayStationName='" + subwayStationName + '\'' +
                ", houseId=" + houseId +
                '}';
    }
}
