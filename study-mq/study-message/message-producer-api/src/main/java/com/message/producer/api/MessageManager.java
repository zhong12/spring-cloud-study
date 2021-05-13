package com.message.producer.api;

import com.message.queue.api.Message;
import com.message.queue.api.MessageResult;
import com.study.common.response.ResultResponse;

/**
 * @Author: zj
 * @Date: 2021/4/29 11:34
 * @Description:
 * @Version: 1.0
 */
public interface MessageManager {
    /**
     * save message record
     *
     * @param message
     * @return
     */
    ResultResponse<Boolean> saveMessageRecord(Message message);

    /**
     * send message queue
     *
     * @param message
     * @param isSynchronization
     * @return
     */
    ResultResponse<MessageResult> sendMessageToQueue(Message message, boolean isSynchronization);
}
