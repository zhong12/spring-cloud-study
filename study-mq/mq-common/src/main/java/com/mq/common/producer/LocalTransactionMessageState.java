package com.mq.common.producer;

/**
 * @Author: zj
 * @Date: 2021/5/18 16:47
 * @Description:
 * @Version: 1.0
 */
public enum LocalTransactionMessageState {
    COMMIT_MESSAGE,
    ROLLBACK_MESSAGE,
    UNKNOW,
}
