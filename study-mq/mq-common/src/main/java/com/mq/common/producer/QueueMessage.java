package com.mq.common.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:50
 * @Description:
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueMessage implements Serializable {
    private static final long serialVersionUID = 1530085243745187350L;
    private String topic;
    private String tag;
    private String key;
    private byte[] body;
}
