package com.itchina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.Column;

/***
 *  @auther xiadongming
 *  @date 2020/7/19
 **/
public class SupportAddressDTO {

    /**
     * @JsonProperty 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，
     * 如把trueName属性序列化为name，@JsonProperty("name")。
     **/
    private Long id;
    @JsonProperty(value = "belong_to")
    private String belongTo;
    @JsonProperty(value = "en_name")
    private String enName;
    @JsonProperty(value = "cn_name")
    private String cnName;

    private String level;

    private double baiduMapLongtitue;
    private  double baiduMapLatitude;


    public void setBaiduMapLongtitue(double baiduMapLongtitue) {
        this.baiduMapLongtitue = baiduMapLongtitue;
    }

    public void setBaiduMapLatitude(double baiduMapLatitude) {
        this.baiduMapLatitude = baiduMapLatitude;
    }

    public double getBaiduMapLongtitue() {
        return baiduMapLongtitue;
    }

    public double getBaiduMapLatitude() {
        return baiduMapLatitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public String getEnName() {
        return enName;
    }

    public String getCnName() {
        return cnName;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "SupportAddressDTO{" +
                "id=" + id +
                ", belongTo='" + belongTo + '\'' +
                ", enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
