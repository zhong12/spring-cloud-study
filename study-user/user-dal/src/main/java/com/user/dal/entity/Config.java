package com.user.dal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: zj
 * @Date: 2021/4/16 11:24
 * @Description:
 * @Version: 1.0
 */
@TableName("config")
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "`describe`")
    private String describe;
    @TableField(value = "`value`")
    private String value;
    @TableField(value = "create_time")
    /**
     * 页面写入数据库时格式化
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库导出页面时json格式化
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField(value = "is_valid")
    private Integer isValid;
}
