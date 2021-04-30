package com.study.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:42
 * @Description:
 * @Version: 1.0
 */
public class ApplicationContextAwareNamelessBean implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextHolder.getApplicationContext() == null) {
            ApplicationContextHolder.setApplicationContext(applicationContext);
        }
    }
}
