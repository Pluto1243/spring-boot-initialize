package com.wj.boot.utils;

import java.util.Base64;

/**
 * 自定义加密解密
 *
 * @author wangjie
 * @date 14:19 2021年09月17日
 **/
public class MyEncryptionUtils {

    // 加密和解密只要offset一致即可
    public static final int offset = Integer.MIN_VALUE + 10086;

    /**
     * @param s
     * @return java.lang.String
     * @description 加密
     * @author wangjie
     * @date 14:19 2021年09月17日
     */
    public static String encryption(String s) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            sb.append("" + (char) (((int) s.charAt(i)) + offset));
        }

        return new StringBuilder(Base64.getEncoder().encodeToString(new String(sb.toString()).getBytes())).reverse().toString();
    }

}
