package com.study.bootstrap;

import com.study.extension.annotation.SPIImplement;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @Author: zj
 * @Date: 2021/4/30 15:44
 * @Description:
 * @Version: 1.0
 */
public class BeanValidatorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] beanNames = configurableListableBeanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanType = configurableListableBeanFactory.getType(beanName);
            SPIImplement spiImplAnnotation = beanType.getAnnotation(SPIImplement.class);
            if (spiImplAnnotation != null) {
                throw new IllegalStateException(
                        "@" + SPIImplement.class.getSimpleName() + " annotation can not coexist with  bean definition ,"
                                + "beanName=[" + beanName + "],type=[" + beanType.getName() + "]");
            }
        }
    }
}
