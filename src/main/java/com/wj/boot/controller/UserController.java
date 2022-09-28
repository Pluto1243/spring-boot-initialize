package com.wj.boot.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wj.boot.entity.User;
import com.wj.boot.entity.request.UserRequest;
import com.wj.boot.entity.response.R;
import com.wj.boot.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制类
 *
 * @author wangjie
 * @date 14:17 2022年07月26日
 **/
@RestController
@Api(tags = "用户")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/userLogin")
    @ApiOperationSupport(order = 101)
    @ApiOperation(value = "用户登录")
    public R<User> userLogin(@RequestBody UserRequest userRequest) {
        return R.ok(userService.userLogin(userRequest.getUsername(), userRequest.getPassword()));
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    @ApiOperationSupport(order = 102)
    public R<Boolean> logout() {
        return R.ok(userService.logout());
    }
}
