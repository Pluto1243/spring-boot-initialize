package com.wj.boot.exception;

/**
 * 异常枚举
 *
 * @author wangjie
 * @date 10:59 2022年07月26日
 **/
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum EmError implements CommonError {
    LOG_FAILD(10014, "日志记录错误"),

    LOGOUT_FAILD(10013, "退出登录失败"),

    LOGIN_FAILD(10012, "登录失败，请检查用户名或密码是否正确"),

    UNKNOWN_ERROR(10011, "未知异常"),

    LOGIN_EXPIRED(10010, "登录过期");

    private int errCode;
    private String errMsg;

    private EmError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
