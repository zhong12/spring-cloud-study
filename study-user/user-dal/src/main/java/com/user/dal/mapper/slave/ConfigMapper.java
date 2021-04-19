package com.user.dal.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.user.dal.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Primary;

/**
 * @Author: zj
 * @Date: 2020/8/11 17:04
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
}
