package com.rocketmq.producer;

import com.message.queue.api.producer.MessageProducer;
import com.mq.common.producer.QueueMessage;
import com.mq.common.producer.SendMessageResult;
import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import com.study.extension.annotation.SPIImplement;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import javax.annotation.Resource;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:57
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@SPIImplement
public class RocketMqMessageProducer implements MessageProducer {

    @Resource(name = "getRocketMQProducer")
    private MQProducer mqProducer;
    @Resource(name = "getTransactionMQProducer")
    private TransactionMQProducer transactionMQProducer;

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

    @Override
    public SendMessageResult sendTransactionMessage(QueueMessage queueMessage) {
        Message message = new Message(queueMessage.getTopic(), queueMessage.getTag(), queueMessage.getKey(), queueMessage.getBody());
        TransactionSendResult sendResult;
        try {
            sendResult = transactionMQProducer.sendMessageInTransaction(message, null);
        } catch (Exception e) {
            throw new MessageSendException(ErrorEnum.MQ_ERROR, e);
        }
        return new SendMessageResult(sendResult.getMsgId());
    }
}
