package com.mq.common.consumer;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:42
 * @Description:
 * @Version: 1.0
 */
@Data
public class ConsumerResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 是否处理成功
     */
    private boolean isSuccess;
    /**
     * 如果处理失败，是否允许消息队列继续调用，直到处理成功，默认true
     */
    private boolean isReconsumeLater = true;
    /**
     * 是否需要记录消费日志，默认不记录
     */
    private boolean isSaveConsumeLog = false;
    /**
     * 错误Code
     */
    private String errCode;
    /**
     * 错误消息
     */
    private String errMsg;
    /**
     * 错误堆栈
     */
    private Throwable e;
    /**
     * 设置错误信息
     * @param errCode
     * @param errMsg
     * @param e
     * 2018年3月1日 zhaowg
     */
    public void setErrInfo(String errCode,String errMsg,Throwable e){
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.e = e;
        this.isSuccess = false;
    }
}
