package com.study.extension.annotation;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:43
 * @Description: Marker for service provider interface
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface SPI {
}
