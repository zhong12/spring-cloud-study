package com.message.queue.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2021/4/29 11:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class MessageResult implements Serializable {
    private static final long serialVersionUID = -404351304052123366L;
    private boolean success;
    private String messageId;
    private String message;
}
