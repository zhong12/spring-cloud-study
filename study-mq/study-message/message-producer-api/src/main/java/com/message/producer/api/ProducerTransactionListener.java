package com.message.producer.api;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/5/18 17:12
 * @Description:
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface ProducerTransactionListener {
    String NOT_EXIST = "NOT_EXIST";

    /**
     * consumerGroup
     *
     * @return
     */
    String producerGroup() default NOT_EXIST;

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
    String subExpression() default "*";
}
