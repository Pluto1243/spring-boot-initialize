package com.wj.boot.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Qian
 */
public class PasswordEncoderUtils {
    static PasswordEncoder passwordEncoder = null;

    static {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 加密
     *
     * @param realPass 真实的密码
     * @return
     */
    public static String encode(String realPass) {
        return passwordEncoder.encode(realPass);
    }

    /**
     * 密文对比
     *
     * @param realPass        真实密码
     * @param encodedPassword 加密后的密码
     * @return
     */
    public static boolean matches(String realPass, String encodedPassword) {
        return passwordEncoder.matches(realPass, encodedPassword);
    }

    /**
     * MD5加密
     *
     * @param encode 要加密的值
     * @param count  需要加密的次数
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encodeMd5(String encode, Long count) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        if (count > 0) {
            md5.update(encode.getBytes());
            count -= 1;
        }

        return new BigInteger(1, md5.digest()).toString(16);
    }

    /**
     * MD5加密
     *
     * @param encode 要加密的值
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encodeMd5(String encode) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(encode.getBytes());
        return new BigInteger(1, md5.digest()).toString(16);
    }
}
