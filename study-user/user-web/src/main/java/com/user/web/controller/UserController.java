package com.user.web.controller;

import com.study.common.response.ResultResponse;
import com.user.web.remote.UserClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/19 11:54
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@Api(tags = {"demo"})
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserClient userClient;

    @GetMapping("")
    public ResultResponse<List> get() {
        return userClient.get();
    }

    @GetMapping("/{name}")
    public ResultResponse<String> sendMq(@PathVariable("name") String name){
        return userClient.sendMq(name);
    }
}
