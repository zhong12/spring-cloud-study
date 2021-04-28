package com.mq.common.producer;

import com.study.common.annotation.SPI;
import com.study.common.exception.MessageSendException;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:45
 * @Description: 消息发送抽象接口
 * @Version: 1.0
 */
@SPI
public interface MessageProducer {
    /**
     * Send messages synchronously
     *
     * @param queueMessage
     * @return
     * @throws MessageSendException
     */
    SendMessageResult send(QueueMessage queueMessage) throws MessageSendException;

    /**
     * Send only once
     *
     * @param queueMessage
     * @return
     * @throws MessageSendException
     */
    void sendOneway(QueueMessage queueMessage);
}
