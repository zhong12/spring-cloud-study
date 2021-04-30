package com.study.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:42
 * @Description:
 * @Version: 1.0
 */
public class ApplicationContextHolder {
    private static volatile ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * get beans of type
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getBeansOfType(Class<T> clazz) {
        Map<String, T> beanMap = applicationContext.getBeansOfType(clazz);
        return new ArrayList<>(beanMap.values());
    }

    /**
     * Get singleton bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getSingleton(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
