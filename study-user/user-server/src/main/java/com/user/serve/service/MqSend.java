package com.user.serve.service;

import com.mq.common.producer.SendMessageResult;

/**
 * @Author: zj
 * @Date: 2021/4/28 16:37
 * @Description:
 * @Version: 1.0
 */
public interface MqSend {
    /**
     * send
     *
     * @param name
     * @return
     */
    SendMessageResult send(String name);
}
