package com.itchina.service.search.constants;

/**
 * @Author: xiadongming
 * @Date: 2020/8/20 21:50
 */
public class HouseIndexMessage {

    public static final String INDEX = "index";

    public static final String REMOVE = "remove";

    public  static final int MAX_RETRY = 3;

    private Integer houseId;
    private String operation;
    private int retry = 0;

    public HouseIndexMessage() {
    }

    public HouseIndexMessage(Integer houseId, String operation, int retry) {
        this.houseId = houseId;
        this.operation = operation;
        this.retry = retry;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public String getOperation() {
        return operation;
    }

    public int getRetry() {
        return retry;
    }
}
