package com.rocketmq.consumer.config;

//import com.rocketmq.consumer.MqConsumeMsgListenerProcessor;

import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:32
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "${rocketmq.consumer.isOnOff}", havingValue = "true")
public class RocketMqConsumerProperties {
    @Value("${rocketmq.consumer.nameSrvAddr}")
    private String nameSrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize:1}")
    private int consumeMessageBatchMaxSize;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws MessageSendException {
        if (StringUtils.isEmpty(nameSrvAddr)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "nameSrvAddr is blank", true);
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        /**
         * Set whether consumer starts consumption from the queue head or the end of the queue for the first time
         * If it is not started for the first time, continue to consume according to the last consumption position
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // Set consumption model, cluster or broadcast, the default is cluster
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // Set the number of messages consumed once, 1 by default
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        return consumer;
    }
}
