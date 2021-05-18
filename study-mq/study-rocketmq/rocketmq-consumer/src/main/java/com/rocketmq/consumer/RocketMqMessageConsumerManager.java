package com.rocketmq.consumer;

import com.message.consumer.api.MqConsumeService;
import com.message.consumer.api.RootMessageConsumer;
import com.message.queue.api.consumer.MessageConsumerConfig;
import com.rocketmq.consumer.reflect.MessageListenerConcurrentlyFactory;
import com.study.bootstrap.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:04
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class RocketMqMessageConsumerManager {

    private final MessageConsumerConfig config;

    public RocketMqMessageConsumerManager(MessageConsumerConfig config) {
        this.config = config;
    }

    public void start() {
        log.info("Start RocketMQ consumer,consumerGroup={},topic={}", config.getConsumerGroup(), config.getTopics());
        DefaultMQPushConsumer consumer = ApplicationContextHolder.getSingleton(DefaultMQPushConsumer.class);
        if (StringUtils.isNotBlank(config.getConsumerGroup()) && !StringUtils.equalsIgnoreCase(config.getConsumerGroup(), MqConsumeService.NOT_EXIST)) {
            consumer.setConsumerGroup(config.getConsumerGroup());
        }
        //  register message listener, now we only support normal message
        MessageListenerConcurrently messageListener = new DefaultMessageListener();
        consumer.registerMessageListener(messageListener);
        // start
        try {
            /**
             *
             * Set the topic and tag that the consumer subscribes to. If the consumer subscribes to all the tags under the topic, the tag uses *;
             * If you need to specify a subscription to some tags under this topic, use | segmentation, such as tag1 | tag2 | tag3
             */
            String[] topicTagsArr = config.getTopics().split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0], topicTag[1]);
            }
            consumer.start();
        } catch (MQClientException e) {
            log.error("RocketMQ Consumer startup failed. Caused by " + e.getErrorMessage(), e);
            throw new RuntimeException("RocketMQ Consumer startup failed.", e);
        }
    }

    private class DefaultMessageListener implements MessageListenerConcurrently {
        private final MessageListenerConcurrently listenerDelegate;

        DefaultMessageListener() {
            Class<? extends RootMessageConsumer> messageConsumerInterface = config.getMessageConsumerInterface();
            listenerDelegate = MessageListenerConcurrentlyFactory.create(messageConsumerInterface, config.getBean());
        }

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
            // !!! default consumeMessageBatchMaxSize is 1, consume one by one
            return listenerDelegate.consumeMessage(messageExtList, context);
        }
    }
}
