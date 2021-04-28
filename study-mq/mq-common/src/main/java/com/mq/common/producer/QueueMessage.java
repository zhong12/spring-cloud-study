package com.mq.common.producer;

import lombok.Data;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:50
 * @Description:
 * @Version: 1.0
 */
@Data
public class QueueMessage {
    private String topic;
    private String tag;
    private String key;
    private byte[] body;
}
