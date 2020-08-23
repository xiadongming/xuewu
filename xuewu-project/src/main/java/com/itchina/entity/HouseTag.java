package com.itchina.entity;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/

import javax.persistence.*;

@Entity
@Table(name = "house_tag")
public class HouseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "house_id")
    private Integer houseId;

    private String name;

    public HouseTag() {

    }

    public HouseTag(Integer houseId, String name) {
        this.houseId = houseId;
        this.name = name;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "HouseTag{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", name='" + name + '\'' +
                '}';
    }
}
