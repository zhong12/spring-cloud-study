package com.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.mq.common.consumer.ConsumerResult;
import com.mq.common.consumer.MqConsumeService;
import com.study.common.exception.ErrorEnum;
import com.study.common.exception.MessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:42
 * @Description: Consumer consumption message routing
 * @Version: 1.0
 */
@Slf4j
@Component
public class MqConsumeMsgListenerProcessor implements MessageListenerConcurrently {
    @Autowired
    private Map<String, MqMsgProcessor> mqMsgProcessorMap;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(messageExtList)) {
            log.info("The received message is empty and will not be processed. Success is returned directly");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        ConsumeConcurrentlyStatus concurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        try {
            //Group by topic
            Map<String, List<MessageExt>> topicGroups = messageExtList.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
            for (Entry<String, List<MessageExt>> topicEntry : topicGroups.entrySet()) {
                String topic = topicEntry.getKey();
                //Group by tags
                Map<String, List<MessageExt>> tagGroups = topicEntry.getValue().stream().collect(Collectors.groupingBy(MessageExt::getTags));
                for (Entry<String, List<MessageExt>> tagEntry : tagGroups.entrySet()) {
                    String tag = tagEntry.getKey();
                    //Consume the message of tag under a certain topic
                    this.consumeMsgForTag(topic, tag, tagEntry.getValue());
                }
            }
        } catch (Exception e) {
            log.error("Failed to process message", e);
            concurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        // If there is no return success, the consumer will consume the message again until return success
        return concurrentlyStatus;
    }

    /**
     * According to topic and tags routing, find consumer message service
     *
     * @param topic
     * @param tag
     * @param value
     */
    private void consumeMsgForTag(String topic, String tag, List<MessageExt> value) {
        // Query specific consumption services according to topic and tag
        MqMsgProcessor imqMsgProcessor = selectConsumeService(topic, tag);
        try {
            if (imqMsgProcessor == null) {
                log.error("According to topic: {} -- tag: {} no corresponding message processing service was found", topic, tag);
                throw new MessageSendException(ErrorEnum.MQ_ERROR, "processing is blank", true);
            }
            log.info("The service that is routed to according to topic: {} -- tag: {} is: to start calling to process messages", topic, tag, imqMsgProcessor.getClass().getName());
            // Call the method of this class to process the message
            ConsumerResult consumerResult = imqMsgProcessor.handle(topic, tag, value);
            if (consumerResult == null) {
                throw new MessageSendException(ErrorEnum.MQ_ERROR, "handle is blank", true);
            }
            if (consumerResult.isSuccess()) {
                log.info("Message processed successfully：" + JSON.toJSONString(consumerResult));
            } else {
                throw new MessageSendException(ErrorEnum.MQ_ERROR, JSON.toJSONString(consumerResult), true);
            }
            if (consumerResult.isSaveConsumeLog()) {
                //TODO 记录消费日志
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * According to topic and tag query corresponding specific consumer services
     *
     * @param topic
     * @param tag
     * @return
     */
    private MqMsgProcessor selectConsumeService(String topic, String tag) {
        MqMsgProcessor processor = null;
        for (Entry<String, MqMsgProcessor> entry : mqMsgProcessorMap.entrySet()) {
            //Get the topic and tags of the annotation on the service implementation class
            MqConsumeService consumeService = entry.getValue().getClass().getAnnotation(MqConsumeService.class);
            if (consumeService == null) {
                log.error("Consumer service：" + entry.getValue().getClass().getName() + "not added on MQConsumeService annotation");
                continue;
            }
            String annotationTopic = consumeService.topic().getCode();
            if (!annotationTopic.equals(topic)) {
                continue;
            }
            String[] tagsArr = consumeService.tags();
            //The "*" sign indicates subscribing to all tags under the topic
            if (tagsArr[0].equals("*")) {
                processor = entry.getValue();
                break;
            }
            boolean isContains = Arrays.asList(tagsArr).contains(tag);
            if (isContains) {
                processor = entry.getValue();
                break;
            }
        }
        return processor;
    }

}