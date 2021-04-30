package com.study.common.response;

import com.study.common.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2020/8/5 15:41
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> implements Serializable {
    private static final long serialVersionUID = 6461874158354311076L;
    /**
     * 任务接受状态
     */
    private boolean success;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 错误信息
     */
    private String code;

    /**
     * 数据
     */
    private T data;

    public static ResultResponse success() {
        return success(null);
    }

    public static ResultResponse success(Object data) {
        return new ResultResponse(true, ErrorEnum.SUCCESS.name(), String.valueOf(ErrorEnum.SUCCESS.getCode()), data);
    }

    public static ResultResponse error(String errorMessage) {
        ErrorEnum errorEnum = ErrorEnum.SYSTEM_ERROR;
        errorEnum.setMessage(errorMessage);
        return error(errorEnum);
    }

    public static ResultResponse error(ErrorEnum errorEnum) {
        return new ResultResponse(false, errorEnum.getMessage(), String.valueOf(errorEnum.getCode()), null);
    }

    public static ResultResponse error(ErrorEnum errorEnum, String errorMessage) {
        return new ResultResponse(false, errorMessage, String.valueOf(errorEnum.getCode()), null);
    }
}
