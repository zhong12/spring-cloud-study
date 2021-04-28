package com.study.common.exception;

/**
 * @Author: zj
 * @Date: 2021/4/15 18:49
 * @Description:
 * @Version: 1.0
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String code;

    public BizException(String code, String message) {
        new BizException(code, message, null);
    }

    public BizException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = String.valueOf(errorEnum.getCode());
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BizException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getMessage(), cause);
        this.code = String.valueOf(errorEnum.getCode());
    }
}
