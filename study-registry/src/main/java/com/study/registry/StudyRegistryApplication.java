package com.study.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhongjing
 */
@EnableEurekaServer
@SpringBootApplication
public class StudyRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyRegistryApplication.class, args);
    }

}
