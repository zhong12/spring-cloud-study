package com.user.serve.config.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author: zj
 * @Date: 2021/4/20 10:25
 * @Description: DynamicDataSourceAspect
 * @Version: 1.0
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {
    private final String[] METHOD_QUERY_PREFIX = {"get"};

    /**
     * Pointcut
     */
    @Pointcut("execution( * com.user.dal.mapper.das.*.*(..)) || execution(* com.baomidou.mybatisplus.extension.service.impl.*.*(..)) ")
    public void daoAspect() {
    }

    /**
     * Before
     *
     * @param point the point
     */
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point) {
        Boolean isQueryMethod = isQueryMethod(point.getSignature().getName());
        if (isQueryMethod) {
            DynamicDataSourceContextHolder.getSecondDataSource();
            log.debug("Switch DataSource to [{}] in Method [{}]",
                    DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
        }
    }

    /**
     * After
     *
     * @param
     */
    @After("daoAspect()")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        log.debug("Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }


    /**
     * Judge data source
     *
     * @param methodName
     * @return
     */
    private Boolean isQueryMethod(String methodName) {
        for (String prefix : METHOD_QUERY_PREFIX) {
            if (methodName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
