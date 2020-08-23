package com.itchina.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: xiadongming
 * @Date: 2020/8/12 20:13
 *  用于搜索的列表框
 */
public class DatatableSearch {

    private int draw;
    private int start;
    private int length;
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMax;

    private String city;
    private String title;
    private String direction;
    private String orderBy;

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTimeMin(Date createTimeMin) {
        this.createTimeMin = createTimeMin;
    }

    public void setCreateTimeMax(Date getCreateTimeMax) {
        this.createTimeMax = getCreateTimeMax;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getDraw() {
        return draw;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTimeMin() {
        return createTimeMin;
    }

    public Date getCreateTimeMax() {
        return createTimeMax;
    }

    public String getCity() {
        return city;
    }

    public String getTitle() {
        return title;
    }

    public String getDirection() {
        return direction;
    }

    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public String toString() {
        return "DatatableSearch{" +
                "draw=" + draw +
                ", start=" + start +
                ", length=" + length +
                ", status=" + status +
                ", createTimeMin=" + createTimeMin +
                ", createTimeMax=" + createTimeMax +
                ", city='" + city + '\'' +
                ", title='" + title + '\'' +
                ", direction='" + direction + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
