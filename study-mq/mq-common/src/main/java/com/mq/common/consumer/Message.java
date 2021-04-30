package com.mq.common.consumer;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2021/4/30 14:31
 * @Description:
 * @Version: 1.0
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 7047618733662181639L;
    private byte[] payLoad;
    private String topic;
    private String tags;
    private String msgId;
    private String msgKey;
    private long bornTimestamp;
    private long storeTimestamp;
    /**
     *
     */
    private int reconsumeTimes;
    private long recvTimestamp;

    public Message(byte[] payLoad) {
        this.payLoad = payLoad;
    }
}
