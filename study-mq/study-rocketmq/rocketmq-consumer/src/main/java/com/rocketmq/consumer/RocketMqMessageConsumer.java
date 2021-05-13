package com.rocketmq.consumer;

import com.message.queue.consumer.MessageConsumer;
import com.message.queue.consumer.MessageConsumerConfig;
import com.study.common.exception.MessageSendException;
import com.study.extension.annotation.SPIImplement;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zj
 * @Date: 2021/4/30 10:54
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@SPIImplement
public class RocketMqMessageConsumer implements MessageConsumer {

    @Override
    public void startMessageConsumer(MessageConsumerConfig config) throws MessageSendException {
        RocketMqMessageConsumerManager manager = new RocketMqMessageConsumerManager(config);
        manager.start();
    }
}
