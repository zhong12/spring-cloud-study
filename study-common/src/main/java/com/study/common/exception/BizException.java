package com.study.common.exception;

import java.text.MessageFormat;

/**
 * @Author: zj
 * @Date: 2021/4/15 18:49
 * @Description:
 * @Version: 1.0
 */
public class BizException extends RuntimeException {
    private Integer code;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public BizException(ErrorEnum errorEnum, String value) {
        super(MessageFormat.format(errorEnum.getMessage(), value));
        this.code = errorEnum.getCode();
    }
}
