package com.wj.boot.service;

import com.wj.boot.config.log.LogAnnotation;
import com.wj.boot.config.log.OperateType;
import com.wj.boot.entity.User;
import lombok.extern.java.Log;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 用户接口
 *
 * @author wangjie
 * @date 11:49 2022年07月26日
 **/
@Validated
public interface IUserService {

    /**
     * 获取用户
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 用户登录
     *
     * @param account
     * @param passwd
     * @return
     */
    User userLogin(@NotNull(message = "请输入账号") String account,
                   @NotNull(message = "请输入密码")String passwd);

    /**
     * @description 退出登录
     *
     * @author wangjie
     * @date 16:57 2022年06月13日
     * @param
     * @return java.lang.Boolean
     */
    Boolean logout();
}
