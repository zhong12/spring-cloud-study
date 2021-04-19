package com.user.dal.mapper.das.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.user.dal.entity.Config;
import com.user.dal.mapper.das.ConfigDas;
import com.user.dal.mapper.master.ConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zj
 * @Date: 2021/4/16 16:46
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
public class ConfigDasImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigDas {

}
