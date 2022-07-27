package com.wj.boot.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户请求类
 *
 * @author wangjie
 * @date 12:11 2022年07月26日
 **/
@Data
public class UserRequest implements Serializable {

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
