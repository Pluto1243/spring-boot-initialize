package com.wj.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author wangjie
 * @date 10:57 2022年07月26日
 **/
@Data
@TableName("user")
@ApiModel("用户")
public class User implements Serializable {

    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("token")
    @TableField(exist = false)
    private String token;
}
