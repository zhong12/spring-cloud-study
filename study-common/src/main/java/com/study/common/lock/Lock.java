package com.study.common.lock;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:35
 * @Description:
 * @Version: 1.0
 */
public class Lock {
    private static final ObjectSimpleCache<String, Lock> LOCK_CACHE = new ObjectSimpleCache<>();

	/*private final String key;

	private Lock(String key) {
		this.key = key;
	}*/

    public static Lock getLock(String key) {
        return LOCK_CACHE.get(key, Lock::new);
    }
}
