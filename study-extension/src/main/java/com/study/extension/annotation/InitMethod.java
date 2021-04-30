package com.study.extension.annotation;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:43
 * @Description: Marker for an init method
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface InitMethod {
}
