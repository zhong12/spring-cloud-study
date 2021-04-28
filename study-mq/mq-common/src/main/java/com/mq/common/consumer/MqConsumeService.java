package com.mq.common.consumer;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:42
 * @Description: This annotation is used to label consumer services
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface MqConsumeService {
    /**
     * 消息主题
     */
     TopicEnum topic();

    /**
     * 消息标签,如果是该主题下所有的标签，使用“*”
     */
     String[] tags();


}
