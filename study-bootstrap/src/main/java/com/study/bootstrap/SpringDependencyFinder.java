package com.study.bootstrap;

import com.study.extension.DependencyFinder;
import com.study.extension.annotation.SPIImplement;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:48
 * @Description:
 * @Version: 1.0
 */
@SPIImplement
public class SpringDependencyFinder implements DependencyFinder {
    @Override
    public <T> T find(Class<T> type, String name) {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        if (context == null) {
            return null;
        }
        Map<String, T> beanMap = context.getBeansOfType(type);
        if (beanMap.isEmpty()) {
            return null;
        }
        // by name
        if (beanMap.keySet().contains(name)) {
            return beanMap.get(name);
        }
        // size==1 ,fault tolerance
        if (beanMap.keySet().size() == 1) {
            return beanMap.values().iterator().next();
        }
        throw new IllegalStateException("Too many beans found,type=[" + type.getSimpleName()
                + "],name=[" + name + "],beans=[" + beanMap + "]");
    }
}
