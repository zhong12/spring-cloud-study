package com.rocketmq.consumer.reflect;

import com.message.consumer.api.RootMessageConsumer;
import com.message.consumer.api.SimpleMessageConsumer;
import com.mq.common.consumer.ConsumeStatus;
import com.mq.common.consumer.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:06
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MessageListenerConcurrentlyFactory {
    public static MessageListenerConcurrently create(Class<? extends RootMessageConsumer> messageConsumerInterface,
                                                     Object bean) {
        if (messageConsumerInterface == SimpleMessageConsumer.class) {
            Method messageConsumeMethod;
            try {
                messageConsumeMethod = SimpleMessageConsumer.class.getMethod("consumeMessage", List.class);
            } catch (NoSuchMethodException e) {
                throw new UnsupportedOperationException();
            }
            return new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
                    List<Message> messageList = buildMessage(messageExtList);
                    Object ret;
                    try {
                        ret = messageConsumeMethod.invoke(bean, messageList);
                    } catch (Exception e) {
                        // return failure
                        log.error("Invoke consumer method error", e);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                    // return by status
                    ConsumeStatus status = (ConsumeStatus) ret;
                    if (status == ConsumeStatus.CONSUME_SUCCESS) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            };
        }
        throw new UnsupportedOperationException();
    }

    public static List<Message> buildMessage(List<MessageExt> messageExtList) {
        List<Message> messageList = new ArrayList<>();
        messageExtList.forEach(messageExt -> {
            Message message = new Message(messageExt.getBody());
            // Manually copy source message fields to target message.
            // Here do not use PropertyUtils.describe(messageExt) instead!!! describe method requires more than five
            // seconds and will slow down messages's consumption, why so slow ? I think maybe messageExt object can not be
            // treated as a normal data object.
            message.setTopic(messageExt.getTopic());
            message.setTags(messageExt.getTags());
            message.setMsgKey(messageExt.getKeys());
            message.setMsgId(messageExt.getMsgId());
            message.setBornTimestamp(messageExt.getBornTimestamp());
            message.setStoreTimestamp(messageExt.getStoreTimestamp());
            message.setReconsumeTimes(messageExt.getReconsumeTimes());
            message.setRecvTimestamp(System.currentTimeMillis());
            messageList.add(message);
        });

        return messageList;
    }
}
