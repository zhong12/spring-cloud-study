package com.user.serve.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:39
 * @Description:
 * @Version: 1.0
 */
@Configuration
@MapperScan(basePackages = "com.user.dal.mapper.slave", sqlSessionFactoryRef = "secondarySqlSessionFactory")
public class SecondaryDataSourceConfig {
    /**
     * @Bean 注册Bean对象
     * @Primary 表示默认数据源
     * @ConfigurationProperties 读取properties中的配置参数映射成为一个对象
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public HikariDataSource getSecondaryDateSource() {
        System.out.println("secondaryDataSource");
        return new HikariDataSource();
    }

    /**
     * @param datasource 数据源
     * @return SqlSessionFactory
     * @Primary 默认SqlSessionFactory
     */
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("secondaryDataSource") DataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(datasource);
        // 设置Mybatis全局配置路径
//        bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/local/*.xml"));
        return bean.getObject();
    }

    @Bean("secondarySessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
