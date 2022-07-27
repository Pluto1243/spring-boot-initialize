package com.wj.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.boot.config.log.LogAnnotation;
import com.wj.boot.config.log.OperateType;
import com.wj.boot.entity.User;
import com.wj.boot.exception.CommonException;
import com.wj.boot.exception.EmError;
import com.wj.boot.mapper.UserMapper;
import com.wj.boot.service.IUserService;
import com.wj.boot.utils.BCryptPasswordEncoderUtil;
import com.wj.boot.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户实现类
 *
 * @author wangjie
 * @date 11:51 2022年07月26日
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BCryptPasswordEncoderUtil passwordEncoderUtil;

    /**
     * 获取用户
     *
     * @param account
     * @return
     */
    @Override
    public User getUserByAccount(String account) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", account));
    }

    /**
     * 用户登录
     *
     * @param account
     * @param passwd
     * @return
     */
    @Override
    @LogAnnotation(description = "账号登录",
        tableName = "user",
        operateType = OperateType.SELECT)
    public User userLogin(String account, String passwd) {
        final User user = getUserByAccount(account);
        if (user == null) {
            throw new CommonException(EmError.LOGIN_FAILD);
        }
        if (passwordEncoderUtil.matches(passwd, user.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            // token放入用户ID
            claims.put(Claims.ID, user.getId());
            // token放入用户电话
            claims.put(Claims.SUBJECT, user.getUsername());
            claims.put(Claims.ISSUED_AT, new Date());
            user.setToken(jwtTokenUtil.generateToken(claims));
            return user;
        }
        throw new CommonException(EmError.LOGIN_FAILD);
    }

    @Override
    @LogAnnotation(description = "TODO 退出登录",
        tableName = "user",
        operateType = OperateType.SELECT)
    public Boolean logout() {
        try {
            // 获取当前请求
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if (token != null) {
                jwtTokenUtil.removeToken(token);
            }
        } catch (IllegalStateException e) {
            throw new CommonException(EmError.LOGOUT_FAILD);
        }
        return true;
    }
}
