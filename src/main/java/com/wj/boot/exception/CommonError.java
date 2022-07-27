package com.wj.boot.exception;

/**
 * 异常接口
 *
 * @author wangjie
 * @date 10:58 2022年07月26日
 **/
public interface CommonError {

    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
