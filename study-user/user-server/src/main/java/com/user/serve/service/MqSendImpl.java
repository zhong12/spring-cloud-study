package com.user.serve.service;

import com.mq.common.producer.QueueMessage;
import com.mq.common.producer.SendMessageResult;
import com.rocketmq.producer.RocketMqMessageProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zj
 * @Date: 2021/4/28 16:37
 * @Description:
 * @Version: 1.0
 */
@Service
public class MqSendImpl implements MqSend {

    @Value("${rocketmq.producer.topic}")
    private String topic;
    @Value("${rocketmq.producer.tag}")
    private String tag;
    @Resource
    private RocketMqMessageProducer producer;

    @Override
    public SendMessageResult send(String name) {
        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setKey(name);
        queueMessage.setTopic(topic);
        queueMessage.setTag(tag);
        queueMessage.setBody(name.getBytes(StandardCharsets.UTF_8));
        SendMessageResult sendMessageResult = producer.send(queueMessage);
        return sendMessageResult;
    }
}
