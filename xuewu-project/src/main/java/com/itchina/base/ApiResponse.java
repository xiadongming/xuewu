package com.itchina.base;

/***
 *  @auther xiadongming
 *  @date 2020/7/16
 **/
public class ApiResponse {
    private int code;
    private String messge;
    private Object data;
    private boolean more;

    public ApiResponse() {
        this.code = Status.SUCCESS.getCode();
        this.messge = Status.SUCCESS.getStandardMessage();
    }

    public ApiResponse(int code, String messge, Object data) {
        this.code = code;
        this.messge = messge;
        this.data = data;
        this.more = true;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessge(String messge) {
        this.messge = messge;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public int getCode() {
        return code;
    }

    public String getMessge() {
        return messge;
    }

    public Object getData() {
        return data;
    }

    public boolean isMore() {
        return more;
    }

    public static ApiResponse ofMessage(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    public static ApiResponse ofSuccess(Object data) {
        return new ApiResponse(Status.SUCCESS.getCode(), Status.SUCCESS.getStandardMessage(), data);
    }

    public static ApiResponse ofStatus(Status status) {
        return new ApiResponse(Status.SUCCESS.getCode(), Status.SUCCESS.getStandardMessage(), null);
    }

    public enum Status {
        //枚举值需要和构造函数保持一致
        SUCCESS(200, "ok"),
        BAD_REQUEST(400, "Bad_Request"),
        INTERNAL_SERVER_ERROR(500, "Unknow internal error"),
        NOT_VALID_PARAM(40005, "Not valid params"),
        NOT_SUPPOR_OPERATION(40006, "operation not support"),
        NOT_LOGIN(50000, "Not login"),
        NOT_FOUND(404,"not found");
        private int code;
        private String standardMessage;

        Status(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }

        public int getCode() {
            return code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }
    }
}
