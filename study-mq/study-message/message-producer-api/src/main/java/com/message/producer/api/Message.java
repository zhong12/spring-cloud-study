package com.message.producer.api;

import com.study.common.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zj
 * @Date: 2021/5/13 14:38
 * @Description:
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message implements Serializable {
    private static final long serialVersionUID = 2030150648024234443L;

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
    /**
     * createTime
     */
    private Long createTime = System.currentTimeMillis();
    /**
     * transactionId
     */
    private String transactionId;

    /**
     * toLoggingString
     *
     * @return
     */
    public String toLoggingString() {
        return String.format("%s|%s|%s|%s|%s", topic, tag, key, content,
                DateTimeUtil.toSecondString(createTime));
    }
}