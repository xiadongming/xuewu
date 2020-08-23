package com.itchina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
public class SubwayDTO {

    private Integer id;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "city_en_name")
    private String cityEnName;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    @Override
    public String toString() {
        return "subwayDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cityEnName='" + cityEnName + '\'' +
                '}';
    }
}
