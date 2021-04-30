package com.study.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:43
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class BootstrapAutoConfiguration {
    @Bean
    public static BeanValidatorBeanFactoryPostProcessor beanValidatorBeanFactoryPostProcessor() {
        return new BeanValidatorBeanFactoryPostProcessor();
    }

    @Bean
    public ApplicationContextAwareNamelessBean applicationContextAwareNamelessBean() {
        return new ApplicationContextAwareNamelessBean();
    }
}
