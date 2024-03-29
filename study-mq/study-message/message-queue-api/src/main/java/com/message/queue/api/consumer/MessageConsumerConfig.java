package com.message.queue.api.consumer;

import com.message.consumer.api.RootMessageConsumer;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @Author: zj
 * @Date: 2021/4/30 14:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class MessageConsumerConfig {
    private final String consumerGroup;
    private final String topics;
    private final Object bean;
    private Class<? extends RootMessageConsumer> messageConsumerInterface;

    public MessageConsumerConfig(String consumerGroup, String topics, Object bean,
                                 Class<? extends RootMessageConsumer> messageConsumerInterface) {
        this.consumerGroup = consumerGroup;
        this.topics = topics;
        this.bean = bean;
        this.messageConsumerInterface = messageConsumerInterface;
    }

    @Override
    public String toString() {
        return String.format("{%s|%s|%s|%s}", consumerGroup, topics, bean.getClass().getName(),
                messageConsumerInterface.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof MessageConsumerConfig)) { return false; }
        MessageConsumerConfig that = (MessageConsumerConfig)o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(consumerGroup, that.consumerGroup)
                .append(topics, that.topics)
                .append(bean, that.bean)
                .append(messageConsumerInterface, that.messageConsumerInterface)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(consumerGroup)
                .append(topics)
                .append(bean)
                .append(messageConsumerInterface)
                .toHashCode();
    }
}
