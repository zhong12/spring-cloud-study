package com.user.web.consumer;

import com.message.consumer.api.MqConsumeService;
import com.mq.common.consumer.ConsumeStatus;
import com.mq.common.consumer.Message;
import com.rocketmq.consumer.starter.AbstractMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: zj
 * @Date: 2021/4/28 19:27
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@MqConsumeService(consumerGroup = "${rocketmq.consumer.groupName}", topic = "${rocketmq.consumer.topic}", tags = "${rocketmq.consumer.tag}")
public class ConsumerMqMsgProcessor extends AbstractMessageConsumer {

    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Override
    public void consumerBeforeProcess(Message message) {
        System.out.println("I'm consumerBeforeProcess");
    }

    @Override
    protected ConsumeStatus consumeMessage(Message message) {
        String msg = new String(message.getPayLoad());
        log.info("获取到的消息为：" + msg);
        //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）

        //如果注解中tags数据中包含多个tag或者是全部的tag(*)，则需要根据tag判断是那个业务，
        //如果注解中tags为具体的某个tag，则该服务就是单独针对tag处理的
        if (message.getTags().equals("某个tag")) {
            //做某个操作
        }
        //TODO 获取该消息重试次数
        int reconsumeTimes = message.getReconsumeTimes();
        //根据消息重试次数判断是否需要继续消费
        if (reconsumeTimes == consumeMessageBatchMaxSize) {
            //消息已经重试了3次，如果不需要再次消费，则返回成功
        }
        return ConsumeStatus.CONSUME_SUCCESS;
    }

    @Override
    public void consumerAfterProcess(Message message, ConsumeStatus consumeStatus) {
        System.out.println("I'm consumerAfterProcess");
    }
}
