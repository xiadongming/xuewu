package com.itchina.base;

/**
 * @Author: xiadongming
 * @Date: 2020/8/13 21:33
 */
public enum HouseStatus {

    NOT_AUTITED(0),//未审核
    PASSES(1),//审核通过
    RENTED(2),//已出租
    DELETED(3);//已删除
    private int value;

    HouseStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
