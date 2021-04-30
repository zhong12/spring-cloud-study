package com.message.consumer.api;

import com.mq.common.consumer.ConsumeStatus;
import com.mq.common.consumer.Message;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/30 14:28
 * @Description:
 * @Version: 1.0
 */
public interface SimpleMessageConsumer extends RootMessageConsumer {

    /**
     * consumer before Process
     * @param message
     */
    default void consumerBeforeProcess(Message message){
        System.out.println("I'm super consumerBeforeProcess");
    }

    /**
     * consume message
     *
     * @param messageList
     * @return
     */
    ConsumeStatus consumeMessage(List<Message> messageList);

    /**
     * consumer after Process
     * @param message
     * @param consumeStatus
     */
    default void consumerAfterProcess(Message message, ConsumeStatus consumeStatus){
        System.out.println("I'm super consumerAfterProcess");
    }
}
