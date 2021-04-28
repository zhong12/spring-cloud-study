package com.rocketmq.consumer.config;

import com.study.common.constant.MqConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author: zj
 * @Date: 2021/4/28 11:54
 * @Description:
 * @Version: 1.0
 */
public class MqConsumerCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // Judge whether the current environment switch is on
        String isOnOff = conditionContext.getEnvironment().getProperty("rocketmq.consumer.isOnOff");
        // Returns true if and only if the value is on
        if(!StringUtils.isEmpty(isOnOff) && isOnOff.equalsIgnoreCase(MqConstant.ON)){
            return true;
        }
        return false;
    }
}
