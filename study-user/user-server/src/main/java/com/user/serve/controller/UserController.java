package com.user.serve.controller;

import com.message.queue.api.MessageResult;
import com.study.common.response.ResultResponse;
import com.user.dal.entity.Config;
import com.user.dal.mapper.das.ConfigDas;
import com.user.serve.service.MqSend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/demo")
public class UserController {
    @Resource
    private ConfigDas configDas;
    @Resource
    private MqSend mqSend;

    @ApiOperation(value="查询配置信息")
    @GetMapping("")
    public ResultResponse<List> get() {
        List<Config> configList = configDas.list();
        return ResultResponse.success(configList);
    }

    @ApiOperation(value="通过id查询配置信息")
    @GetMapping("/{id}")
    public ResultResponse<Config> getId(@PathVariable("id") Long id) {
        Config config = configDas.getById(id);
        return ResultResponse.success(config);
    }

    @ApiOperation(value="通过name发送mq消息")
    @GetMapping("/send/{name}")
    public ResultResponse<MessageResult> sendMq(@PathVariable("name") String name) {
        ResultResponse<MessageResult> response = mqSend.send(name);
        return response;
    }

}
