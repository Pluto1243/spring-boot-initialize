package com.wj.boot.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 响应类-分页
 *
 * @author wangjie
 * @date 11:56 2022年07月26日
 **/
@Data
@Accessors(chain = true)
public class ResPage<T> implements Serializable {
    /** 默认最大每页个数 */
    private static final int MAX_LIMIT = 1000;
    /** 默认每页个数 */
    private static final int DEFAULT_LIMIT = 10;
    /** 默认从第一页开始 */
    private static final int DEFAULT_PAGE = 1;
    private long code;
    private Object data;
    private String message;
    private String sorts;

    public ResPage() {
        this.code = code;
        this.message = message;
        HashMap<String, Object> pageArgs = new HashMap<>();
        pageArgs.put("data", data);
        pageArgs.put("page", DEFAULT_PAGE);
        pageArgs.put("limit", DEFAULT_LIMIT);
        pageArgs.put("totalCount", 0);
        this.data = pageArgs;
    }

    public ResPage(long code, T data, String message, int page, int limit, int totalCount) {
        this.code = code;
        this.message = message;
        HashMap<String, Object> pageArgs = new HashMap<>();
        pageArgs.put("data", data);
        pageArgs.put("page", page);
        pageArgs.put("limit", limit);
        pageArgs.put("totalCount", totalCount);
        this.data = pageArgs;
    }

    public static <T> ResPage<T> ok(T data, int page, int limit, Long totalCount) {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = ApiErrorCode.FAILED;
        }
        return restResult(data, aec, page, limit, totalCount);
    }

    public static <T> ResPage<T> ok() {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;
        return restResult(aec);
    }

    public static <T> ResPage<T> error(T data) {
        ApiErrorCode aec = ApiErrorCode.FAILED;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = ApiErrorCode.FAILED;
        }
        return restResult(data, aec);
    }

    public static <T> ResPage<T> error() {
        ApiErrorCode aec = ApiErrorCode.FAILED;
        return restResult(aec);
    }

    public static <T> ResPage<T> failed(String msg) {
        return restResult(null, ApiErrorCode.FAILED.getCode(), msg);
    }

    public static <T> ResPage<T> restResult(T data, IErrorCode errorCode, int page, int limit, Long totalCount) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg(), page, limit, totalCount);
    }

    public static <T> ResPage<T> restResult(T data, IErrorCode errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> ResPage<T> restResult(IErrorCode errorCode) {
        return restResult(errorCode.getCode(), errorCode.getMsg());
    }

    private static <T> ResPage<T> restResult(T data, long code, String msg) {
        ResPage<T> result = new ResPage<>();
        result.setCode(code);
        result.setData(data);
        result.setMessage(msg);
        return result;
    }

    private static <T> ResPage<T> restResult(T data, long code, String msg, int page, int limit, Long totalCount) {
        ResPage<T> result = new ResPage<>();
        result.setCode(code);
        HashMap<String, Object> pageArgs = new HashMap<>(16);
        pageArgs.put("data", data);
        pageArgs.put("page", page);
        pageArgs.put("limit", limit);
        pageArgs.put("totalCount", totalCount);
        result.setData(pageArgs);
        result.setMessage(msg);
        return result;
    }

    private static <T> ResPage<T> restResult(long code, String msg) {
        ResPage<T> result = new ResPage<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
