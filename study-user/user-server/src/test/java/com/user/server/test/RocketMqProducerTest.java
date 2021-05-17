package com.user.server.test;

import com.user.serve.UserServeApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: zj
 * @Date: 2021/5/17 16:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServeApplication.class)
public class RocketMqProducerTest {
    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 使用自己封装的生产者
     */
//    @Autowired
//    private MQProducerSendMsgProcessor producerSendMsgProcessor;
    @Test
    public void send() throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        String msg = "test1234";
        log.info("开始发送消息：" + msg);
        Message sendMsg = new Message("test", "tag", msg.getBytes());
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        log.info("消息发送响应信息：" + sendResult.toString());
    }
}
