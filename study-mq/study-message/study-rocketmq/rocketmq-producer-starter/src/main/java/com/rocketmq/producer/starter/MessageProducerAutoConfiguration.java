package com.rocketmq.producer.starter;

import com.message.producer.api.MessageManager;
import com.rocketmq.producer.TransactionMessageProducerManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zj
 * @Date: 2021/4/29 18:48
 * @Description:
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "rocketmq.producer", value = "isOnOff", havingValue = "true")
public class MessageProducerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MessageManager.class)
    public MessageManager messageManager() {
        return new RocketMqMessageManager();
    }

    @Bean(name = "messageProducerAnnotationBeanPostProcessor")
    public MessageProducerAnnotationBeanPostProcessor messageProducerAnnotationBeanPostProcessor() {
        TransactionMessageProducerManager producerManager = new TransactionMessageProducerManager();
        return new MessageProducerAnnotationBeanPostProcessor(producerManager);
    }
}
