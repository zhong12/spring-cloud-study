package com.rocketmq.consumer.starter;

import com.message.queue.api.consumer.MessageConsumer;
import com.mq.common.constant.RocketMqConstant;
import com.rocketmq.consumer.config.RocketMqConsumerProperties;
import com.study.extension.ExtensionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zj
 * @Date: 2021/4/30 11:41
 * @Description:
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "true")
public class MessageConsumerAutoConfiguration {
    @Autowired
    private RocketMqConsumerProperties rocketMqConsumerProperties;

    @Bean(name = "messageConsumerAnnotationBeanPostProcessor")
    public MessageConsumerAnnotationBeanPostProcessor messageConsumerAnnotationBeanPostProcessor() {
        MessageConsumer consumerManager = ExtensionLoader.getExtensionLoader(MessageConsumer.class).getByName(RocketMqConstant.rocketMqQueue);
        return new MessageConsumerAnnotationBeanPostProcessor(consumerManager);
    }
}
