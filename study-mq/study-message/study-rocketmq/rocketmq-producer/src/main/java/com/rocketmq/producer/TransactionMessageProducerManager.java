package com.rocketmq.producer;

import com.message.producer.api.ProducerTransactionListener;
import com.message.queue.api.producer.TransactionMessageProducerConfig;
import com.rocketmq.producer.reflect.TransactionListenerConcurrentlyFactory;
import com.study.bootstrap.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:36
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class TransactionMessageProducerManager {

    public void start(TransactionMessageProducerConfig config) {
        log.info("Start Transaction producer,producerGroup={},topic={}", config.getProducerGroup(), config.getTopic());
        TransactionMQProducer transactionMQProducer = ApplicationContextHolder.getSingleton(TransactionMQProducer.class);
        if (StringUtils.isNotBlank(config.getProducerGroup()) && !StringUtils.equalsIgnoreCase(config.getProducerGroup(), ProducerTransactionListener.NOT_EXIST)) {
            transactionMQProducer.setProducerGroup(config.getProducerGroup());
        }
        //  register message listener, now we only support normal message
        transactionMQProducer.setTransactionListener(TransactionListenerConcurrentlyFactory.create(config.getTransactionMessageListenerInterface(), config.getBean()));
        // start
        try {
            transactionMQProducer.start();
            log.info(String.format("Transaction producer is start ! groupName:[%s] | nameSrvAddr:[%s]", transactionMQProducer.getProducerGroup(), transactionMQProducer.getNamesrvAddr()));
        } catch (MQClientException e) {
            log.error("RocketMQ Transaction producer startup failed. Caused by " + e.getErrorMessage(), e);
            throw new RuntimeException("RocketMQ Transaction producer startup failed.", e);
        }
    }
}
