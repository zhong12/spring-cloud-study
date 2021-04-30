package com.message.queue.api.consumer;

import com.study.common.exception.MessageSendException;
import com.study.extension.annotation.SPI;

/**
 * @Author: zj
 * @Date: 2021/4/29 19:19
 * @Description:
 * @Version: 1.0
 */
@SPI
public interface MessageConsumer {
    /**
     * start message consumer
     */
    void startMessageConsumer(MessageConsumerConfig config) throws MessageSendException;
}
