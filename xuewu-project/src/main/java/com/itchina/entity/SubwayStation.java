package com.itchina.entity;

import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
@Entity
@Table(name = "subway_station")
public class SubwayStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "subway_id")
    private Integer subwayId;
    @Column(name = "name")
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSubwayId(Integer subwayId) {
        this.subwayId = subwayId;
    }

    public Integer getSubwayId() {
        return subwayId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SubwayStation{" +
                "id=" + id +
                ", subwayId=" + subwayId +
                ", name='" + name + '\'' +
                '}';
    }
}
