package com.study.common.exception;

public class CacheException extends BizException {

    public CacheException(String code, String message) {
        super(code, message);
    }

    public CacheException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public CacheException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

    /**
     * 封装异常
     *
     * @param errorEnum
     * @param errMsg
     * @param isTransfer 是否转换异常信息，如果为true,则直接使用errMsg信息
     */
    public CacheException(ErrorEnum errorEnum, String errMsg, Boolean isTransfer) {
        super(String.valueOf(errorEnum.getCode()), isTransfer ? errMsg : errorEnum.getMessage());
    }
}
