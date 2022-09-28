package com.wj.boot.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应类
 *
 * @author wangjie
 * @date 12:00 2022年07月26日
 **/
@Data
@Accessors(chain = true)
public class R<T>  implements Serializable {
    private long code;
    private T data;
    private String message;

    public R() {

    }

    public R(long code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> R<T> ok(T data) {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = ApiErrorCode.FAILED;
        }
        return restResult(data, aec);
    }

    public static <T> R<T> ok() {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;

        return restResult(aec);
    }

    public static <T> R<T> error(T data) {
        ApiErrorCode aec = ApiErrorCode.FAILED;
        return restResult(data, aec);
    }

    public static <T> R<T> error() {
        ApiErrorCode aec = ApiErrorCode.FAILED;
        return restResult(aec);
    }

    public static <T> R<T> error(ApiErrorCode aec) {
        return restResult(aec);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, ApiErrorCode.FAILED.getCode(), msg);
    }

    public static <T> R<T> restResult(T data, IErrorCode errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> R<T> restResult(IErrorCode errorCode) {
        return restResult(errorCode.getCode(), errorCode.getMsg());
    }

    private static <T> R<T> restResult(T data, long code, String msg) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setData(data);
        result.setMessage(msg);
        return result;
    }

    private static <T> R<T> restResult(long code, String msg) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
