package com.mq.common.consumer;

/**
 * @Author: zj
 * @Date: 2021/4/28 14:42
 * @Description: Define message queue topics
 * @Version: 1.0
 */
public enum TopicEnum {
	DemoTopic("DemoTopic","示例主题"),
	DemoNewTopic("DemoNewTopic","示例主题新"),
	;
	
	private String code;
    private String msg;

    private TopicEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
