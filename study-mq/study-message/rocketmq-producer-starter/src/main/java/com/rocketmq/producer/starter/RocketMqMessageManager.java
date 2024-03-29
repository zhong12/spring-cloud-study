package com.rocketmq.producer.starter;

import com.message.producer.api.Message;
import com.message.producer.api.MessageManager;
import com.message.queue.api.MessageResult;
import com.message.queue.api.producer.MessageProducer;
import com.mq.common.constant.RocketMqConstant;
import com.mq.common.producer.QueueMessage;
import com.mq.common.producer.SendMessageResult;
import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import com.study.common.response.ResultResponse;
import com.study.extension.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author: zj
 * @Date: 2021/4/29 12:03
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class RocketMqMessageManager implements MessageManager {

    @Override
    public ResultResponse<Boolean> saveMessageRecord(Message message) {
        log.info("Save message to record:{}", message.toLoggingString());
        ResultResponse response = ResultResponse.success();
        return response;
    }

    @Override
    public ResultResponse<MessageResult> sendMessageToQueue(Message message, boolean isSynchronization) {
        QueueMessage queueMessage = QueueMessage.builder().topic(message.getTopic()).tag(message.getTag())
                .key(message.getKey()).body(message.getContent().getBytes(StandardCharsets.UTF_8)).build();
        ResultResponse response = ResultResponse.success();
        try {
            log.info("Commit message to queue:{}", message.toLoggingString());
            MessageProducer messageProducer = ExtensionLoader.getExtensionLoader(MessageProducer.class).getByName(RocketMqConstant.rocketMqQueue);
            if (isSynchronization) {
                SendMessageResult sendMessageResult = messageProducer.send(queueMessage);
                response.setData(sendMessageResult);
            } else {
                messageProducer.sendOneway(queueMessage);
            }
        } catch (MessageSendException e) {
            log.error("Commit message error >> {} - {}", message.toLoggingString(), e);
            response = ResultResponse.error(ErrorEnum.MQ_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("Commit message error >> {} - {}", message.toLoggingString(), e);
            response = ResultResponse.error(e.getMessage());
        }
        log.info("Commit message response:{}", response);
        return response;
    }

    @Override
    public ResultResponse<MessageResult> sendTransactionMessageToQueue(Message message) {
        QueueMessage queueMessage = QueueMessage.builder().topic(message.getTopic()).tag(message.getTag())
                .key(message.getKey()).body(message.getContent().getBytes(StandardCharsets.UTF_8)).build();
        ResultResponse response = ResultResponse.success();
        try {
            log.info("Commit transaction message to queue:{}", message.toLoggingString());
            SendMessageResult sendMessageResult = ExtensionLoader.getExtensionLoader(MessageProducer.class)
                    .getByName(RocketMqConstant.rocketMqQueue).sendTransactionMessage(queueMessage);
            response.setData(sendMessageResult);
        } catch (MessageSendException e) {
            log.error("Commit transaction message error >> {} - {}", message.toLoggingString(), e);
            response = ResultResponse.error(ErrorEnum.MQ_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("Commit transaction message error >> {} - {}", message.toLoggingString(), e);
            response = ResultResponse.error(e.getMessage());
        }
        log.info("Commit transaction message response:{}", response);
        return response;
    }

}
