package com.study.common.utils;

import lombok.Data;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:48
 * @Description:
 * @Version: 1.0
 */
@Data
public class OrderedObject implements Comparable<OrderedObject>{
    private final Object obj;
    private final int order;

    public OrderedObject(Object obj, int order) {
        this.obj = obj;
        this.order = order;
    }

    @Override
    public int compareTo(OrderedObject o) {
        return this.order - o.order;
    }
}
