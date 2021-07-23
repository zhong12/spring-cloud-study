package com.registry.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RegistryNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryNacosApplication.class, args);
    }

}
