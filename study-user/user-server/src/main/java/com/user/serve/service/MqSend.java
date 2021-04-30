package com.user.serve.service;

import com.message.queue.api.MessageResult;
import com.study.common.response.ResultResponse;

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
    ResultResponse<MessageResult> send(String name);
}
