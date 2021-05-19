package com.message.producer.api;

import lombok.Data;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class MessageExt {
    /**
     * topic
     */
    private String topic;
    /**
     * tag
     */
    private String tag;
    /**
     * key
     */
    private String key;
    /**
     * content
     */
    private String content;
    private int queueId;
    private int storeSize;
    private long queueOffset;
    private int sysFlag;
    private long bornTimestamp;
    private long storeTimestamp;
    private String msgId;
    private long commitLogOffset;
    private int bodyCRC;
    private int reconsumeTimes;
    private long preparedTransactionOffset;
}
