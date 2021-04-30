package com.rocketmq.consumer.starter;

import com.message.queue.api.consumer.MessageConsumer;
import com.rocketmq.consumer.config.RocketMqConsumerCondition;
import com.study.extension.ExtensionLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zj
 * @Date: 2021/4/30 11:41
 * @Description:
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties(RocketMqConsumerCondition.class)
public class MessageConsumerAutoConfiguration {

    @Bean(name = "messageConsumerAnnotationBeanPostProcessor")
    public MessageConsumerAnnotationBeanPostProcessor messageConsumerAnnotationBeanPostProcessor() {
        MessageConsumer consumerManager = ExtensionLoader.getExtensionLoader(MessageConsumer.class).getAdaptiveExtension();
        return new MessageConsumerAnnotationBeanPostProcessor(consumerManager);
    }
}
