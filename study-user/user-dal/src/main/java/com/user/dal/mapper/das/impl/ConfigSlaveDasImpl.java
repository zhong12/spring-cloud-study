package com.user.dal.mapper.das.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.user.dal.entity.Config;
import com.user.dal.mapper.das.ConfigSlaveDas;
import com.user.dal.mapper.slave.ConfigSlaveMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: zj
 * @Date: 2021/4/19 16:49
 * @Description:
 * @Version: 1.0
 */
@Service
public class ConfigSlaveDasImpl extends ServiceImpl<ConfigSlaveMapper, Config> implements ConfigSlaveDas {
}
