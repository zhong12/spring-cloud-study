package com.rocketmq.consumer.starter;

import com.message.consumer.api.SimpleMessageConsumer;
import com.mq.common.consumer.ConsumeStatus;
import com.mq.common.consumer.Message;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/30 16:17
 * @Description:
 * @Version: 1.0
 */
public abstract class AbstractMessageConsumer implements SimpleMessageConsumer {

    @Override
    public ConsumeStatus consumeMessage(List<Message> messageList) {
        ConsumeStatus consumeStatus = null;
        for (Message message : messageList) {
            // Preprocessing
            consumerBeforeProcess(message);
            // Service Consumer message
            consumeStatus = this.consumeMessage(message);
            // Post processing
            consumerAfterProcess(message, consumeStatus);
        }
        return consumeStatus;
    }

    /**
     * consumer message
     *
     * @param message
     * @return
     */
    protected abstract ConsumeStatus consumeMessage(Message message);
}
