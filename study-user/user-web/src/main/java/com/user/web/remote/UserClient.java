package com.user.web.remote;

import com.study.common.response.ResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/28 19:17
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "user-server")
public interface UserClient {
    @GetMapping("")
    public ResultResponse<List> get();

    @GetMapping("/{name}")
    public ResultResponse<String> sendMq(@PathVariable("name") String name);
}
