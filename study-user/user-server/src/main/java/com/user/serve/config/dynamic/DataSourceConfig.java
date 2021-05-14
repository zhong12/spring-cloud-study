package com.user.serve.config.dynamic;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zj
 * @Date: 2021/4/20 16:58
 * @Description: DataSourceConfig
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.user.dal.mapper.dao")
public class DataSourceConfig {
    /**
     * primaryDataSource
     *
     * @return
     */
    @Bean("primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * secondDataSource
     *
     * @return
     */
    @Bean("secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * Dynamic data source.
     *
     * @return
     */
    @Bean("dynamicDataSource")
    @DependsOn("initFluentConfiguration")
    public DataSource dynamicDataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource,
                                        @Qualifier("secondDataSource") DataSource secondDataSource) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.master.name(), primaryDataSource);
        dataSourceMap.put(DataSourceKey.second.name(), secondDataSource);
        dynamicRoutingDataSource.setDefaultTargetDataSource(primaryDataSource);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());
        DynamicDataSourceContextHolder.secondDataSourceKeys.addAll(dataSourceMap.keySet());
        // Remove master
        DynamicDataSourceContextHolder.secondDataSourceKeys.remove(DataSourceKey.master.name());
        return dynamicRoutingDataSource;
    }

    /**
     * sqlSessionFactory
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dynamicDataSource(primaryDataSource(), secondDataSource()));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        // 乐观锁插件
//        PerformanceInterceptor(),OptimisticLockerInterceptor()
        // 分页插件
        sqlSessionFactory.setPlugins(Lists.newArrayList(paginationInterceptor()).toArray(new Interceptor[1]));
        return sqlSessionFactory.getObject();
    }

    /**
     * transactionManager
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
}
