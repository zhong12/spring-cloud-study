package com.rocketmq.producer.starter;

import com.message.queue.api.MessageManager;
import com.rocketmq.producer.config.RocketMqProducerCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zj
 * @Date: 2021/4/29 18:48
 * @Description:
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties(RocketMqProducerCondition.class)
public class MessageProducerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MessageManager.class)
    public MessageManager messageManager() {
        return new RocketMqMessageManager();
    }

    @Bean
    @ConditionalOnBean(MessageManager.class)
    public MessageManager messageSender() {
        return new TransactionMessageManager();
    }
}
