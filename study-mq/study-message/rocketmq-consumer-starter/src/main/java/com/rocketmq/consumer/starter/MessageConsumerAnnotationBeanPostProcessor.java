package com.rocketmq.consumer.starter;

import com.message.consumer.api.MqConsumeService;
import com.message.consumer.api.RootMessageConsumer;
import com.message.queue.api.consumer.MessageConsumer;
import com.message.queue.api.consumer.MessageConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zj
 * @Date: 2021/4/30 14:20
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MessageConsumerAnnotationBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton, ApplicationContextAware {

    private MessageConsumer messageConsumer;
    private Environment environment;
    private Set<MessageConsumerConfig> messageConsumerConfigs = new HashSet<>();
    private static final String PLACE_HOLDER_PREFIX = "${";
    private static final String PLACE_HOLDER_SUFFIX = "}";

    MessageConsumerAnnotationBeanPostProcessor(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void afterSingletonsInstantiated() {
        messageConsumerConfigs.forEach(messageConsumer::startMessageConsumer);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.environment = applicationContext.getBean(Environment.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        MqConsumeService messageConsumerAnnotation = targetClass.getAnnotation(MqConsumeService.class);
        if (messageConsumerAnnotation != null) {
            //  consumerGroup
            String consumerGroup = messageConsumerAnnotation.consumerGroup();
            if (StringUtils.isBlank(consumerGroup) || StringUtils.equalsIgnoreCase(consumerGroup, MqConsumeService.NOT_EXIST)) {
                throw new RuntimeException("Consumer group config error, bean=" + targetClass.getName());
            }
            // topic
            String topics = messageConsumerAnnotation.topic();
            if (StringUtils.isBlank(topics) || StringUtils.equalsIgnoreCase(topics, MqConsumeService.NOT_EXIST)) {
                throw new RuntimeException("Topic config error, bean=" + targetClass.getName());
            }
            // find consumer interface
            List<Class<?>> messageConsumerInterfaceList = Arrays.stream(
                    ClassUtils.getAllInterfacesForClass(targetClass)).filter(RootMessageConsumer.class::isAssignableFrom)
                    .collect(Collectors.toList());
            if (messageConsumerInterfaceList.isEmpty() || messageConsumerInterfaceList.size() > 1) {
                throw new RuntimeException("Wrong consumer config,bean=" + targetClass.getName());
            }
            Class<? extends RootMessageConsumer> messageConsumerInterface
                    = (Class<? extends RootMessageConsumer>) messageConsumerInterfaceList.get(0);

            MessageConsumerConfig messageConsumerConfig = new MessageConsumerConfig(
                    resolvePlaceHolder(consumerGroup), resolvePlaceHolder(topics), bean, messageConsumerInterface);
            messageConsumerConfigs.add(messageConsumerConfig);
        }
        return bean;
    }

    private String resolvePlaceHolder(String param) {
        if (param.startsWith(PLACE_HOLDER_PREFIX) && param.endsWith(PLACE_HOLDER_SUFFIX)) {
            return environment.resolvePlaceholders(param);
        }
        return param;
    }
}
