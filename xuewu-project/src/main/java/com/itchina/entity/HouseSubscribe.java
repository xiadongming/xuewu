package com.itchina.entity;

/***
 *  @auther xiadongming
 *  @date 2020/8/9
 **/

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "house_subscribe")
public class HouseSubscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "house_id")
    private Integer houseId;

    @Column(name = "user_id")
    private Integer userId;

    private String desc;
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @Column(name = "order_time")
    private Date orderTime;

    private String telephone;

    @Column(name = "admin_id")
    private Integer adminId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public Integer getAdminId() {
        return adminId;
    }

    @Override
    public String toString() {
        return "HouseSubscribe{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", userId=" + userId +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", orderTime=" + orderTime +
                ", telephone='" + telephone + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
