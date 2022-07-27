package com.wj.boot.exception;

/**
 * 通用异常
 *
 * @author wangjie
 * @date 14:21 2022年07月26日
 **/
public class CommonException extends RuntimeException implements CommonError {

    private CommonError commonError;

    public CommonException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public CommonException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }


    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
