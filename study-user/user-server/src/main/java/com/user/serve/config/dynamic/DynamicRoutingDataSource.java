package com.user.serve.config.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:30
 * @Description: determineCurrentLookupKey
 * @Version: 1.0
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String datasource = DynamicDataSourceContextHolder.getDataSourceKey();
        log.debug("current dataSource {}", datasource);
        return datasource;
    }
}
