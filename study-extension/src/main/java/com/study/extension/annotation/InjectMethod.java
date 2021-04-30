package com.study.extension.annotation;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:42
 * @Description: Marker for an inject method
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface InjectMethod {
    String value() default "";
}
