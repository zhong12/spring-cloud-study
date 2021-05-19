package com.rocketmq.producer.starter;

import com.message.consumer.api.RootMessageConsumer;
import com.message.producer.api.ProducerTransactionListener;
import com.message.producer.api.TransactionMessageListener;
import com.message.queue.api.producer.TransactionMessageProducerConfig;
import com.rocketmq.producer.TransactionMessageProducerManager;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zj
 * @Date: 2021/5/18 17:57
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MessageProducerAnnotationBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton, ApplicationContextAware {
    private TransactionMessageProducerManager transactionMessageProducerManager;
    private Environment environment;
    private Set<TransactionMessageProducerConfig> transactionMessageProducerConfigs = new HashSet<>();
    private static final String PLACE_HOLDER_PREFIX = "${";
    private static final String PLACE_HOLDER_SUFFIX = "}";

    MessageProducerAnnotationBeanPostProcessor(TransactionMessageProducerManager transactionMessageProducerManager) {
        this.transactionMessageProducerManager = transactionMessageProducerManager;
    }

    @Override
    public void afterSingletonsInstantiated() {
        transactionMessageProducerConfigs.forEach(transactionMessageProducerManager::start);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.environment = applicationContext.getBean(Environment.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        ProducerTransactionListener transactionListener = targetClass.getAnnotation(ProducerTransactionListener.class);
        if (Objects.nonNull(transactionListener)) {
            //  consumerGroup
            String producerGroup = transactionListener.producerGroup();
            if (StringUtils.isBlank(producerGroup) || StringUtils.equalsIgnoreCase(producerGroup, ProducerTransactionListener.NOT_EXIST)) {
                throw new RuntimeException("ProducerGroup config error, bean=" + targetClass.getName());
            }
            // topic
            String topics = transactionListener.topic();
            if (StringUtils.isBlank(topics) || StringUtils.equalsIgnoreCase(topics, ProducerTransactionListener.NOT_EXIST)) {
                throw new RuntimeException("Topic config error, bean=" + targetClass.getName());
            }
            // find consumer interface
            List<Class<?>> transactionMessageListenerInterfaceList = Arrays.stream(
                    ClassUtils.getAllInterfacesForClass(targetClass)).filter(TransactionMessageListener.class::isAssignableFrom)
                    .collect(Collectors.toList());
            if (transactionMessageListenerInterfaceList.isEmpty() || transactionMessageListenerInterfaceList.size() > 1) {
                throw new RuntimeException("Wrong transactionMessageListener config,bean=" + targetClass.getName());
            }
            Class<? extends TransactionMessageListener> transactionMessageListener = (Class<? extends TransactionMessageListener>) transactionMessageListenerInterfaceList.get(0);
            TransactionMessageProducerConfig transactionMessageProducerConfig = new TransactionMessageProducerConfig(
                    resolvePlaceHolder(producerGroup), resolvePlaceHolder(topics), resolvePlaceHolder(transactionListener.subExpression()),
                    bean, transactionMessageListener);
            transactionMessageProducerConfigs.add(transactionMessageProducerConfig);
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
