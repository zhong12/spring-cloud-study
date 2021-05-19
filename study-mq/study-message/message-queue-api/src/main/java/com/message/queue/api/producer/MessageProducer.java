package com.message.queue.api.producer;

import com.mq.common.producer.QueueMessage;
import com.mq.common.producer.SendMessageResult;
import com.study.common.exception.MessageSendException;
import com.study.extension.annotation.SPI;

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

    /**
     * Send Transaction messages
     *
     * @param queueMessage
     * @return
     */
    SendMessageResult sendTransactionMessage(QueueMessage queueMessage);
}
