package com.wj.boot.entity.response;

/**
 * 错误接口
 *
 * @author wangjie
 * @date 14:17 2022年07月26日
 **/
public interface IErrorCode {
    /**
     * 错误编码 -1、失败 0、成功
     */
    long getCode();

    /**
     * 错误描述
     */
    String getMsg();
}
