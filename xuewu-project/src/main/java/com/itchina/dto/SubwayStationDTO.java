package com.itchina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public class SubwayStationDTO {

    private Integer id;
    @JsonProperty(value = "subway_id")
    private Integer subwayId;
    private String name;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setSubwayId(Integer subwayId) {
        this.subwayId = subwayId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSubwayId() {
        return subwayId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SubwayStationdto{" +
                "id=" + id +
                ", subwayId=" + subwayId +
                ", name='" + name + '\'' +
                '}';
    }
}
