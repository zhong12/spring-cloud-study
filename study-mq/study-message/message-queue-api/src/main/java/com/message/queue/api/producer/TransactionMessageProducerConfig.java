package com.message.queue.api.producer;

import com.message.producer.api.TransactionMessageListener;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:41
 * @Description:
 * @Version: 1.0
 */
@Data
public class TransactionMessageProducerConfig {
    private String producerGroup;
    private String topic;
    private Object bean;
    private String subExpression;
    private Class<? extends TransactionMessageListener> transactionMessageListenerInterface;

    public TransactionMessageProducerConfig(String producerGroup, String topic, String subExpression, Object bean,
                                            Class<? extends TransactionMessageListener> transactionMessageListenerInterface) {
        this.producerGroup = producerGroup;
        this.topic = topic;
        this.subExpression = subExpression;
        this.bean = bean;
        this.transactionMessageListenerInterface = transactionMessageListenerInterface;
    }

    @Override
    public String toString() {
        return String.format("{%s|%s|%s|%s|%s}", producerGroup, topic, subExpression, bean.getClass().getName(),
                transactionMessageListenerInterface.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionMessageProducerConfig)) {
            return false;
        }
        TransactionMessageProducerConfig that = (TransactionMessageProducerConfig) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(producerGroup, that.producerGroup)
                .append(topic, that.topic)
                .append(subExpression, that.subExpression)
                .append(bean, that.bean)
                .append(transactionMessageListenerInterface, that.transactionMessageListenerInterface)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(producerGroup)
                .append(topic)
                .append(subExpression)
                .append(bean)
                .append(transactionMessageListenerInterface)
                .toHashCode();
    }
}
