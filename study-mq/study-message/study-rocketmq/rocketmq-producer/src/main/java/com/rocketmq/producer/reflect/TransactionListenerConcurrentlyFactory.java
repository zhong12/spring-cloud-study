package com.rocketmq.producer.reflect;

import com.message.producer.api.TransactionMessageListener;
import com.mq.common.producer.LocalTransactionMessageState;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.Method;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:30
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class TransactionListenerConcurrentlyFactory {

    public static TransactionListener create(Class<? extends TransactionMessageListener> transactionMessageListener, Object bean) {
        if (transactionMessageListener == TransactionMessageListener.class) {
            Method executeLocalTransactionMethod;
            Method checkLocalTransactionMethod;
            try {
                executeLocalTransactionMethod = TransactionMessageListener.class.getMethod("executeLocalTransaction", com.message.producer.api.Message.class, Object.class);
                checkLocalTransactionMethod = TransactionMessageListener.class.getMethod("checkLocalTransaction", com.message.producer.api.MessageExt.class);
            } catch (NoSuchMethodException e) {
                throw new UnsupportedOperationException();
            }
            return new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                    Object ret;
                    try {
                        ret = executeLocalTransactionMethod.invoke(bean, buildMessage(message), arg);
                    } catch (Exception e) {
                        // return failure
                        log.error("Invoke transactionMessageListener executeLocalTransaction error", e);
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    // return by status
                    LocalTransactionMessageState status = (LocalTransactionMessageState) ret;
                    if (status == LocalTransactionMessageState.COMMIT_MESSAGE) {
                        return LocalTransactionState.COMMIT_MESSAGE;
                    } else if (status == LocalTransactionMessageState.ROLLBACK_MESSAGE) {
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    return LocalTransactionState.UNKNOW;
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt message) {
                    Object ret;
                    try {
                        ret = checkLocalTransactionMethod.invoke(bean, buildMessageExt(message));
                    } catch (Exception e) {
                        // return failure
                        log.error("Invoke transactionMessageListener checkLocalTransaction error", e);
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    // return by status
                    LocalTransactionMessageState status = (LocalTransactionMessageState) ret;
                    if (status == LocalTransactionMessageState.COMMIT_MESSAGE) {
                        return LocalTransactionState.COMMIT_MESSAGE;
                    } else if (status == LocalTransactionMessageState.ROLLBACK_MESSAGE) {
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                    return LocalTransactionState.UNKNOW;
                }
            };
        }
        throw new UnsupportedOperationException();
    }

    public static com.message.producer.api.MessageExt buildMessageExt(MessageExt msg) {
        com.message.producer.api.MessageExt messageExt = new com.message.producer.api.MessageExt();
        messageExt.setQueueId(msg.getQueueId());
        try {
            String body = new String(msg.getBody(), "UTF-8");
            messageExt.setContent(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageExt.setKey(msg.getKeys());
        messageExt.setTopic(msg.getTopic());
        messageExt.setTag(msg.getTags());
        messageExt.setStoreSize(msg.getStoreSize());
        messageExt.setQueueOffset(msg.getQueueOffset());
        messageExt.setSysFlag(msg.getSysFlag());
        messageExt.setBornTimestamp(msg.getBornTimestamp());
        messageExt.setStoreTimestamp(msg.getStoreTimestamp());
        messageExt.setMsgId(msg.getMsgId());
        messageExt.setCommitLogOffset(msg.getCommitLogOffset());
        messageExt.setBodyCRC(msg.getBodyCRC());
        messageExt.setReconsumeTimes(msg.getReconsumeTimes());
        messageExt.setPreparedTransactionOffset(msg.getPreparedTransactionOffset());
        return messageExt;
    }

    public static com.message.producer.api.Message buildMessage(Message msg) {
        com.message.producer.api.Message message = new com.message.producer.api.Message();
        message.setTopic(msg.getTopic());
        try {
            String body = new String(msg.getBody(), "UTF-8");
            message.setContent(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setKey(msg.getKeys());
        message.setTag(msg.getTags());
        message.setTransactionId(msg.getTransactionId());
        return message;
    }
}
