package com.study.common.exception;

/**
 * @author zhongjing
 */
public enum ErrorEnum {
    SUCCESS(0, "success"),
    SYSTEM_ERROR(-1, "system error"),
    PARAM_NOT_EXIST(1001, "param not exist"),
    HTTP_ERROR(1002, "http fail"),
    JSON_ERROR(1003, "json fail"),
    DATE_ERROR(1004, "date fail"),
    JS_ERROR(1005, "analysis js fail"),
    LOGIN_ERROR(1006, "login fail"),
    MD5_ERROR(1007, "md5 fail"),
    MQ_ERROR(1008, "mq fail"),
    ;
    private Integer code;
    private String message;

    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
