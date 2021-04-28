package com.rocketmq.producer.send;

import com.mq.common.producer.MessageProducer;
import com.mq.common.producer.QueueMessage;
import com.mq.common.producer.SendMessageResult;
import com.rocketmq.producer.config.MqProducerCondition;
import com.study.common.annotation.SPIImplement;
import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:57
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@SPIImplement
@Conditional(MqProducerCondition.class)
@Component
public class RocketMqMessageProducer implements MessageProducer {

    @Resource
    private MQProducer mqProducer;

    @Override
    public SendMessageResult send(QueueMessage queueMessage) throws MessageSendException {
        Message message = new Message(queueMessage.getTopic(), queueMessage.getTag(), queueMessage.getKey(), queueMessage.getBody());
        // send rocketMQ message
        SendResult sendResult;
        try {
            sendResult = mqProducer.send(message);
        } catch (Exception e) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, e);
        }
        return new SendMessageResult(sendResult.getMsgId());
    }

    @Override
    public void sendOneway(QueueMessage queueMessage) {
        Message message = new Message(queueMessage.getTopic(), queueMessage.getTag(), queueMessage.getKey(), queueMessage.getBody());
        try {
            mqProducer.sendOneway(message);
        } catch (MQClientException | RemotingException | InterruptedException e) {
            log.error("sendOneway send message fail:{}", e.getMessage());
        }
    }
}
