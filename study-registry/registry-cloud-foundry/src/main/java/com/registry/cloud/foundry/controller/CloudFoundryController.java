package com.registry.cloud.foundry.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/foundry")
public class CloudFoundryController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取所有注册到CloudFoundry服务
     */
    @GetMapping
    public void hello() {
        discoveryClient.getServices().forEach(discovery -> {
            discoveryClient.getInstances(discovery).forEach(si -> log.info("instance = {}", si));
        });
    }
}
