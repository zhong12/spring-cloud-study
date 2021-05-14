package com.study.extension;

import com.study.extension.annotation.SPI;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:20
 * @Description:
 * @Version: 1.0
 */
@SPI
public interface DependencyFinder {
    /**
     * Find dependency
     *
     * @param type class type
     * @param name class name
     * @param <T>  return Dependency
     * @return
     */
    <T> T find(Class<T> type, String name);
}
