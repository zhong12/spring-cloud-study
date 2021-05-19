package com.message.producer.api;

import com.mq.common.producer.LocalTransactionMessageState;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:44
 * @Description:
 * @Version: 1.0
 */
public interface TransactionMessageListener {
    /**
     * Perform local transactions (executeLocalTransaction)
     *
     * @param message
     * @param arg
     * @return
     */
    LocalTransactionMessageState executeLocalTransaction(Message message, Object arg);

    /**
     * Check local transaction results (checkLocalTransaction)
     *
     * @param message
     * @return
     */
    LocalTransactionMessageState checkLocalTransaction(MessageExt message);
}
