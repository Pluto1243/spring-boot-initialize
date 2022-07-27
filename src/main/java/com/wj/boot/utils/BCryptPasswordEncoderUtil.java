package com.wj.boot.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密类
 *
 * @author wangjie
 * @date 11:58 2022年07月26日
 **/
@Component
public class BCryptPasswordEncoderUtil extends BCryptPasswordEncoder {

    /**
     * 加密
     *
     * @param rawPassword  真实密码
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }

    /**
     * 对比密码
     *
     * @param rawPassword  真实密码
     * @param encodedPassword  加密密文
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return super.matches(rawPassword,encodedPassword);
    }
}
