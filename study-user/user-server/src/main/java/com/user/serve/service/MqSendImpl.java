package com.user.serve.service;

import com.message.queue.api.Message;
import com.message.queue.api.MessageManager;
import com.message.queue.api.MessageResult;
import com.study.common.response.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zj
 * @Date: 2021/4/28 16:37
 * @Description:
 * @Version: 1.0
 */
@Service
public class MqSendImpl implements MqSend {

    @Value("${rocketmq.producer.topic}")
    private String topic;
    @Value("${rocketmq.producer.tag}")
    private String tag;
    @Resource
    private MessageManager messageManager;

    @Override
    public ResultResponse<MessageResult> send(String name) {
        Message message = new Message();
        message.setKey(name);
        message.setTopic(topic);
        message.setTag(tag);
        message.setContent(name);
        ResultResponse<MessageResult> response = messageManager.sendMessageToQueue(message, true);
        return response;
    }
}
