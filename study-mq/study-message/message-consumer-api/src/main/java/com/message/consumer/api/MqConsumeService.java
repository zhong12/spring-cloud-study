package com.message.consumer.api;

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
    String NOT_EXIST = "NOT_EXIST";

    /**
     * consumerGroup
     *
     * @return
     */
    String consumerGroup() default NOT_EXIST;

    /**
     * topic
     *
     * @return
     */
    String topic() default NOT_EXIST;

    /**
     * tags
     *
     * @return
     */
    String tags() default "*";
}
