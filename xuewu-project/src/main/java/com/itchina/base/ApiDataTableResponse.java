package com.itchina.base;

/**
 * @Author: xiadongming
 * @Date: 2020/8/12 20:07
 */
public class ApiDataTableResponse extends ApiResponse {

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;

    public ApiDataTableResponse(ApiResponse.Status status){
       this(status.getCode(),status.getStandardMessage(),null);
    }

    public ApiDataTableResponse(int code, String messge, Object data) {
        super(code, messge, data);
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    @Override
    public String toString() {
        return "ApiDataTableResponse{" +
                "draw=" + draw +
                ", recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                '}';
    }
}
