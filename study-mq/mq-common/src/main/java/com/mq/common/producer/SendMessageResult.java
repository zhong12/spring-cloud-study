package com.mq.common.producer;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:47
 * @Description:
 * @Version: 1.0
 */
public class SendMessageResult {
    /**
     * messageId
     */
    private String messageId;

    public SendMessageResult(String messageId) {
        this.messageId = messageId;
    }
}
