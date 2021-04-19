package com.user.serve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:30
 * @Description:  main()
 * @Version: 1.0
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.user","com.user.serve"},
        exclude = {FlywayAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class UserServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServeApplication.class, args);
    }

}
