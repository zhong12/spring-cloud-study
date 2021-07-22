package com.registry.consul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsulController {

    @RequestMapping("/hello")
    public String hello() {
        return "helle consul";
    }
}
