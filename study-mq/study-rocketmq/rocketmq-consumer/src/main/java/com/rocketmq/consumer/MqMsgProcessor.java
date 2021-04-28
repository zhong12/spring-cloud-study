package com.rocketmq.consumer;

import com.mq.common.consumer.ConsumerResult;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:42
 * @Description: Message queuing - Message consumption processing interface
 * @Version: 1.0
 */
public interface MqMsgProcessor {
	/**
	 * message handling<br/>
	 * If there is no return true, the consumer will consume the message again until return true<br/>
	 * The consumer may consume the message repeatedly. Please do the repeat call processing on the business side.
	 * The interface is designed to be idempotent
	 * @param topic
	 * @param tag
	 * @param messageExtList
	 * @return
	 */
	ConsumerResult handle(String topic, String tag, List<MessageExt> messageExtList);
}
