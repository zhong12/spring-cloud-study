package com.user.serve.transaction.listener;

import com.message.producer.api.Message;
import com.message.producer.api.MessageExt;
import com.message.producer.api.ProducerTransactionListener;
import com.message.producer.api.TransactionMessageListener;
import com.mq.common.producer.LocalTransactionMessageState;
import com.study.common.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zj
 * @Date: 2021/5/19 10:15
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@ProducerTransactionListener(producerGroup = "${rocketmq.producer.groupName}", topic = "${rocketmq.producer.topic}",
        subExpression = "${rocketmq.producer.tag}")
public class UserServerTransactionListener implements TransactionMessageListener {

    private AtomicInteger transactionIndex = new AtomicInteger(0);
    private AtomicInteger checkTimes = new AtomicInteger(0);
    private ConcurrentHashMap<String, Integer> localTransactionMessage = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionMessageState executeLocalTransaction(Message message, Object arg) {
        log.info("start executeLocalTransaction, message:{}", message);
        String msgKey = message.getKey();
        LocalTransactionMessageState state;
        if (msgKey.equals("msg-1")) {
            // 第一条消息让他通过
            state = LocalTransactionMessageState.COMMIT_MESSAGE;
        } else if (msgKey.equals("msg-2")) {
            // 第二条消息模拟异常，明确回复回滚操作
            state = LocalTransactionMessageState.ROLLBACK_MESSAGE;
        } else {
            // 第三条消息无响应，让它调用回查事务方法
            state = LocalTransactionMessageState.UNKNOW;
            // 给剩下3条消息，放1，2，3三种状态
            localTransactionMessage.put(msgKey, transactionIndex.incrementAndGet());
        }
        log.info("executeLocalTransaction:{},execute state:{},current time：", message.getKey(), state, DateTimeUtil.toSecondString(System.currentTimeMillis()));
        return state;
    }

    @Override
    public LocalTransactionMessageState checkLocalTransaction(MessageExt message) {
        log.info("start checkLocalTransaction, message:{}", message);
        String msgKey = message.getKey();
        Integer state = Objects.isNull(localTransactionMessage.get(msgKey)) ? 0 : localTransactionMessage.get(msgKey);
        LocalTransactionMessageState messageState = LocalTransactionMessageState.COMMIT_MESSAGE;
        switch (state) {
            case 1:
                messageState = LocalTransactionMessageState.UNKNOW;
                break;
            case 2:
                messageState = LocalTransactionMessageState.COMMIT_MESSAGE;
                break;
            case 3:
                messageState = LocalTransactionMessageState.ROLLBACK_MESSAGE;
                break;
        }
        log.info("checkLocalTransaction result: {}, Review times:{}", messageState.name(), checkTimes.incrementAndGet());
        if (messageState != LocalTransactionMessageState.COMMIT_MESSAGE) {
            state += 1;
            localTransactionMessage.put(msgKey, state);
        }
        return messageState;
    }
}
