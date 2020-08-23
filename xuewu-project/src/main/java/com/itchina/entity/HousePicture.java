package com.itchina.entity;

import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/
@Entity
@Table(name = "house_picture")
public class HousePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "house_id")
    private Integer houseId;

    @Column(name = "cdn_prefix")
    private String cdnPrefix;

    private Integer width;
    private Integer height;
    private String location;
    private String path;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public void setCdnPrefix(String cdnPrefix) {
        this.cdnPrefix = cdnPrefix;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public String getCdnPrefix() {
        return cdnPrefix;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getLocation() {
        return location;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "HousePicture{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", cdnPrefix='" + cdnPrefix + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", location='" + location + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
