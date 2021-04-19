package com.user.dal.mapper.slave;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.user.dal.entity.Config;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zj
 * @Date: 2020/8/11 17:04
 * @Description:
 * @Version: 1.0
 */
@Mapper
@DS("second")
public interface ConfigSlaveMapper extends BaseMapper<Config> {
}
