package com.rocketmq.producer.starter;

import com.message.producer.api.MessageManager;
import com.rocketmq.producer.config.RocketMqProducerProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RocketMqProducerProperties rocketMqProducerProperties;

    @Bean
    @ConditionalOnMissingBean(MessageManager.class)
    public MessageManager messageManager() {
        return new RocketMqMessageManager();
    }

//    @Bean(name = "transactionMessageManager")
//    @ConditionalOnBean(MessageManager.class)
//    public MessageManager messageSender() {
//        return new TransactionMessageManager();
//    }
}
