package com.rocketmq.producer.config;

import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: zj
 * @Date: 2021/4/27 18:59
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
public class RocketMqProducerProperties {
    /**
     * Sending the same type of message is set to the same group to ensure uniqueness.
     * It does not need to be set by default. Rocketmq will use it ip@pid (PID stands
     * for the name of the JVM) as the unique identifier
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.nameSrvAddr}")
    private String nameSrvAddr;
    /**
     * message max size，default 4M
     */
    @Value("${rocketmq.producer.maxMessageSize:4096}")
    private Integer maxMessageSize;
    /**
     * message send timeout，default 3s
     */
    @Value("${rocketmq.producer.sendMsgTimeout:3000}")
    private Integer sendMsgTimeout;
    /**
     * The number of times the message failed to be sent and retry，default 2
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed:2}")
    private Integer retryTimesWhenSendFailed;

    @Bean(value = "getRocketMQProducer")
    @ConditionalOnMissingBean(MQProducer.class)
    public MQProducer getRocketMQProducer() throws MessageSendException {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "groupName is blank", true);
        }
        if (StringUtils.isEmpty(this.nameSrvAddr)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "nameServerAddr is blank", true);
        }
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.nameSrvAddr);
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        producer.setVipChannelEnabled(false);
        producer.setInstanceName(this.groupName + "123");
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

    @Bean(value = "getTransactionMQProducer")
    @ConditionalOnMissingBean(TransactionMQProducer.class)
    public TransactionMQProducer getTransactionMQProducer() throws MessageSendException {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "groupName is blank", true);
        }
        if (StringUtils.isEmpty(this.nameSrvAddr)) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, "nameServerAddr is blank", true);
        }
        TransactionMQProducer producer = new TransactionMQProducer(this.groupName);
        producer.setNamesrvAddr(this.nameSrvAddr);
        producer.setInstanceName(this.groupName+"456");
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        producer.setVipChannelEnabled(false);
        return producer;
    }
}
