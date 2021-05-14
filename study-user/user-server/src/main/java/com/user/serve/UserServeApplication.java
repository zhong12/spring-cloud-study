package com.user.serve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:30
 * @Description:  main()
 * @Version: 1.0
 */
@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.user.dal","com.user.serve"},
        exclude = {FlywayAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class UserServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServeApplication.class, args);
    }

}
