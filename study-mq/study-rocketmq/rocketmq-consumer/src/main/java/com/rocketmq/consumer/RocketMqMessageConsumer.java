package com.rocketmq.consumer;

import com.message.queue.api.consumer.MessageConsumer;
import com.message.queue.api.consumer.MessageConsumerConfig;
import com.rocketmq.consumer.config.RocketMqConsumerCondition;
import com.study.common.exception.MessageSendException;
import com.study.extension.annotation.SPIImplement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;

/**
 * @Author: zj
 * @Date: 2021/4/30 10:54
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@SPIImplement
@Conditional(RocketMqConsumerCondition.class)
public class RocketMqMessageConsumer implements MessageConsumer {

    @Override
    public void startMessageConsumer(MessageConsumerConfig config) throws MessageSendException {
        RocketMqMessageConsumerManager manager = new RocketMqMessageConsumerManager(config);
        manager.start();
    }
}
