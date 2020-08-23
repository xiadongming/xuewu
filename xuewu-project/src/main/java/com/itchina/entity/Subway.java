package com.itchina.entity;

import javax.persistence.*;

/***
 *  @auther xiadongming
 *  @date 2020/8/8
 **/
@Entity
@Table(name = "subway")
public class Subway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "city_en_name")
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
        return "subway{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cityEnName='" + cityEnName + '\'' +
                '}';
    }
}
