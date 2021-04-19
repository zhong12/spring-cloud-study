package com.user.serve.controller;

import com.study.common.response.ResultResponse;
import com.user.dal.entity.Config;
import com.user.dal.mapper.das.ConfigDas;
import com.user.dal.mapper.das.ConfigSlaveDas;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    private ConfigDas configDas;
    @Autowired
    private ConfigSlaveDas configSlaveDas;

    @GetMapping("/index")
    public ResultResponse<List> index() {
        List<Config> configList = configDas.list();
        return ResultResponse.success(configList);
    }

    @GetMapping("/index1")
    public ResultResponse<List> index1() {
        List<Config> configList = configSlaveDas.list();
        return ResultResponse.success(configList);
    }
}
