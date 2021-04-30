package com.rocketmq.producer.config;

import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @Author: zj
 * @Date: 2021/4/27 18:59
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@SpringBootConfiguration
@Conditional(MqProducerCondition.class)
public class RocketMqProperties {
    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.nameSrvAddr}")
    private String nameSrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize:4096}")
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout:3}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed:2}")
    private Integer retryTimesWhenSendFailed;

    @Bean
    @ConditionalOnMissingBean(MQProducer.class)
    public MQProducer getRocketMQProducer() throws MessageSendException {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "groupName is blank", true);
        }
        if (StringUtils.isEmpty(this.nameSrvAddr)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "nameServerAddr is blank", true);
        }
        DefaultMQProducer producer;
        producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.nameSrvAddr);
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        // If the message fails to be sent, set the number of retries, which is 2 by default
        if (this.retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        try {
            producer.start();
            log.info(String.format("producer is start ! groupName:[%s] | nameSrvAddr:[%s]", this.groupName, this.nameSrvAddr));
        } catch (MQClientException e) {
            log.error(String.format("producer is error {}", e.getMessage(), e));
            throw new MessageSendException(ErrorEnum.MQ_ERROR, e);
        }
        return producer;
    }
}
