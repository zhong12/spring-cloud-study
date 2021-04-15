package com.study.common.exception;

/**
 * @author zhongjing
 */
public enum ErrorEnum {
    SUCCESS(0, "success"),
    NOT_EXIST(1001, "{0} not exist"),
    HTTP_ERROR(1002, "http fail:{0}"),
    JSON_ERROR(1003, "json fail:{0}"),
    DATE_ERROR(1004, "date fail:{0}"),
    JS_ERROR(1005, "analysis js fail:{0}"),
    LOGIN_ERROR(1006, "login fail:{0}"),
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
