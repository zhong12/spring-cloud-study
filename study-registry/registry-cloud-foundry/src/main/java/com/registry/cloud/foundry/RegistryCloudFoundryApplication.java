package com.registry.cloud.foundry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.cloudfoundry.discovery.EnableCloudFoundryClient;
import org.springframework.context.annotation.Bean;
import sun.rmi.runtime.Log;

@Slf4j
@EnableCloudFoundryClient
@SpringBootApplication
public class RegistryCloudFoundryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryCloudFoundryApplication.class, args);
    }
}
