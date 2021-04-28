package com.rocketmq.consumer.config;

import com.rocketmq.consumer.MqConsumeMsgListenerProcessor;
import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import javax.annotation.Resource;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:32
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Conditional(MqConsumerCondition.class)
@SpringBootConfiguration
public class MqConsumerConfiguration {
    @Value("${rocketmq.consumer.nameSrvAddr}")
    private String nameSrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize:1}")
    private int consumeMessageBatchMaxSize;
    @Resource
    private MqConsumeMsgListenerProcessor mqMessageListenerProcessor;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws MessageSendException {
        if (StringUtils.isEmpty(groupName)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "groupName is blank", true);
        }
        if (StringUtils.isEmpty(nameSrvAddr)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "nameSrvAddr is blank", true);
        }
        if (StringUtils.isEmpty(topics)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "topics is blank", true);
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mqMessageListenerProcessor);
        /**
         * Set whether consumer starts consumption from the queue head or the end of the queue for the first time
         * If it is not started for the first time, continue to consume according to the last consumption position
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // Set consumption model, cluster or broadcast, the default is cluster
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        // Set the number of messages consumed once, 1 by default
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            /**
             * Set the subject and tag of the consumer subscription. If it is subscribing to all tags under the topic,
             * tag uses *; If you need to specify a subscription to some tags under this topic,
             * use || split, for example tag1 || tag2 || tag3
             */
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0], topicTag[1]);
            }
            consumer.start();
            log.info("consumer is start !!! groupName:{},topics:{},nameSrvAddr:{}", groupName, topics, nameSrvAddr);
        } catch (MQClientException e) {
            log.error("consumer is start !!! groupName:{},topics:{},nameSrvAddr:{}", groupName, topics, nameSrvAddr, e);
            throw new MessageSendException(ErrorEnum.MQ_ERROR, e);
        }
        return consumer;
    }
}
