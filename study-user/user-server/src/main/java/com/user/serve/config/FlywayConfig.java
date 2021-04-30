package com.user.serve.config;

import com.study.common.exception.BizException;
import com.study.common.exception.ErrorEnum;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:30
 * @Description:  Flyway配置
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class FlywayConfig {

    @Resource
    @Qualifier("primaryDataSource")
    private HikariDataSource dataSource;

    private static String tableExitsMysql = "SELECT COUNT(*) FROM `information_schema`.`TABLES` WHERE table_schema= 'evo_interface' AND table_name = 'flyway_schema_history';";
    private static String deleteUnSuccessMysql = "DELETE FROM `evo_interface`.`flyway_schema_history` WHERE success=0;";
    private static String deleteVersionMysql = "DELETE FROM `evo_interface`.`flyway_schema_history` WHERE version=\"%s\";";
    private static final String IGNORE_EXCEPTION = "already exists";
    private static final String SCHEMA_PATTERN = "{schema}";
    private static final String createDataBaseSql = "CREATE SCHEMA IF NOT EXISTS `{schema}` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;";

    @Value("${spring.flyway.locations}")
    private String locations;
    @Value("${spring.flyway.table}")
    private String flywayTable;

    /**
     * 执行创建数据库
     *
     * @throws SQLException
     */
    private void before() throws SQLException, IOException {
        log.info("DatabaseInitializer uses flyway init-sql to initiate database");
        String url = dataSource.getJdbcUrl();
        // jdbc url最后一个 '/' 用于分割具体 schema?参数
        int lastSplitIndex = url.lastIndexOf('/') + 1;
        // 获取数据库名
        String schema = url.contains("?") ? url.substring(lastSplitIndex, url.indexOf("?")) : url.substring(lastSplitIndex);
        // 获取spring.datasource.url具体数据库schema前的jdbc url
        String addressUrl = url.substring(0, lastSplitIndex);
        // 直连数据库地址:jdbc:mysql://yourIp:port
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(addressUrl);
        hikariDataSource.setUsername(dataSource.getUsername());
        hikariDataSource.setPassword(dataSource.getPassword());
        hikariDataSource.setDriverClassName(dataSource.getDriverClassName());
        Connection connection = hikariDataSource.getConnection();
        Statement statement = connection.createStatement();
        try {
            int count = statement.executeUpdate(createDataBaseSql.contains(SCHEMA_PATTERN) ? createDataBaseSql.replace(SCHEMA_PATTERN, schema) : createDataBaseSql);
            if (count < 1) {
                throw new BizException(ErrorEnum.DATE_ERROR);
            }
        } catch (Exception e) {
            // 异常信息包含 'already exists' 则忽略，否则抛出
            if (!e.getLocalizedMessage().contains(IGNORE_EXCEPTION)) {
                throw e;
            }
        } finally {
            statement.close();
            connection.close();
            hikariDataSource.close();
        }
        Connection tableConnection = dataSource.getConnection();
        try {
            // 保证执行之后脚本之后回归evo_basic数据库
            PreparedStatement preparedStatement = tableConnection.prepareStatement(tableExitsMysql);
            ResultSet set = preparedStatement.executeQuery();
            int result = 0;
            while (set.next()) {
                result = set.getInt(1);
            }
            preparedStatement.close();
            if (result == 0) {
                ScriptRunner runner = new ScriptRunner(tableConnection);
                Resources.setCharset(StandardCharsets.UTF_8);
                runner.setLogWriter(null);
                // 关闭自动提交
                runner.setAutoCommit(false);
                // 则遇到错误停止并抛出异常
                runner.setStopOnError(true);
                // 执行脚本
                Reader baseReader = Resources.getResourceAsReader("db/init/flyway_schema_history.sql");
                runner.runScript(baseReader);
            }
        } finally {
            tableConnection.close();
        }
        log.info("DatabaseInitializer initialize completed");
    }

    @Bean(name = "initFluentConfiguration")
    public FluentConfiguration init() throws Exception {
        before();
        FluentConfiguration fluentConfiguration = Flyway.configure().baselineDescription("init-Flyway-Migration")
                .baselineOnMigrate(true).dataSource(dataSource).outOfOrder(true).table(flywayTable).locations(locations).encoding("UTF-8").cleanDisabled(false);
        Flyway flyway = fluentConfiguration.load();
        while (true) {
            Connection connection = dataSource.getConnection();
            String sql = deleteUnSuccessMysql;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            try {
                preparedStatement.execute();
                log.info("Deleted unsuccessful record in `evo_interface`.`flyway_schema_history`!");
                flyway.migrate();
                break;
            } catch (FlywayException e) {
                String message = e.getMessage();
                String sqlString = deleteVersionMysql;
                String version = null;
                if (message != null && e.getMessage().contains("Validate failed: Migration checksum mismatch for migration version")) {
                    int index = message.trim().lastIndexOf("version") + 8;
                    version = message.substring(index);
                    int last = version.indexOf("\n");
                    version = version.substring(0, last);
                    sql = String.format(sqlString, version);
                } else if (message != null && message.contains("Validate failed: Detected applied migration not resolved locally:")) {
                    version = message.replace("Validate failed: Detected applied migration not resolved locally:", "").trim();
                    sql = String.format(sqlString, version);
                } else if (message != null && message.contains("Validate failed: Migration description mismatch for migration version")) {
                    version = message.replace("Validate failed: Migration description mismatch for migration version", "").trim();
                    int last = version.indexOf("\n");
                    version = version.substring(0, last);
                    sql = String.format(sqlString, version);
                } else {
                    throw e;
                }
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
                log.warn("Delete version {} in flyway_schema_history!", version);
            } finally {
                preparedStatement.close();
                connection.close();
            }
        }
        return fluentConfiguration;
    }
}
