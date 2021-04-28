package com.rocketmq.consumer;

import com.mq.common.consumer.ConsumerResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/28 15:22
 * @Description:
 * @Version: 1.0
 */
public abstract class AbstractMqMsgProcessor implements MqMsgProcessor {

    /**
     * consumer before process
     *
     * @param topic
     * @param tag
     * @param messageExt
     */
    public abstract void consumerBeforeProcess(String topic, String tag, MessageExt messageExt);

    @Override
    public ConsumerResult handle(String topic, String tag, List<MessageExt> messageExtList) {
        ConsumerResult mqConsumeResult = new ConsumerResult();
        for (MessageExt messageExt : messageExtList) {
            // Preprocessing
            consumerBeforeProcess(topic, tag, messageExt);
            // Service Consumer message
            mqConsumeResult = this.consumeMessage(tag, messageExt.getKeys() == null ? null : Arrays.asList(messageExt.getKeys().split(MessageConst.KEY_SEPARATOR)), messageExt);
            // Post processing
            consumerAfterProcess(topic, tag, messageExt, mqConsumeResult);
        }
        return mqConsumeResult;
    }

    /**
     * consumer message
     *
     * @param tag
     * @param keys
     * @param messageExt
     * @return
     */
    protected abstract ConsumerResult consumeMessage(String tag, List<String> keys, MessageExt messageExt);

    /**
     * consumer after process
     *
     * @param topic
     * @param tag
     * @param messageExt
     * @param mqConsumeResult
     */
    public abstract void consumerAfterProcess(String topic, String tag, MessageExt messageExt, ConsumerResult mqConsumeResult);
}
