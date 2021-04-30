package com.study.extension.annotation;

import java.lang.annotation.*;

/**
 * @Author: zj
 * @Date: 2021/4/28 10:59
 * @Description: Marker for spi implement
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIImplement {
    String PRODUCT_APPLY_ALL = "PRODUCT_APPLY_ALL";
    int LOWEST_PRECEDENCE = 2147483647;
    int HIGHEST_PRECEDENCE = -2147483648;

    String warehouseIds() default "PRODUCT_APPLY_ALL";

    String name() default "";

    int order() default 2147483647;
}
