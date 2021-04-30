package com.study.extension.annotation;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:26
 * @Description: Marker for adaptive extension
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {
}
