package com.study.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:35
 * @Description:
 * @Version: 1.0
 */
public final class ObjectSimpleCache<K, V> {
    private final ConcurrentMap<K, V> cache = new ConcurrentHashMap<>();

    /**
     * lock: object VS byte[0]
     */
    private final byte[] lock = new byte[0];

    /**
     * @param key      key
     * @param supplier supplier, requires high performance
     * @return result
     */
    public V get(K key, Supplier<V> supplier) {
        V object = cache.get(key);
        if (object == null && supplier != null) {
            synchronized (lock) {
                object = cache.get(key);
                if (object == null) {
                    V value = supplier.get();
                    if (value != null) {
                        // putIfAbsent is no logger needed,as put operation must acquire lock
                        cache.put(key, value);
                    }
                    object = value;
                }
            }
        }
        return object;
    }
}
