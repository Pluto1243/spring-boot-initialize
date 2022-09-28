package com.wj.boot.exception;

import com.wj.boot.entity.response.R;
import com.wj.boot.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常
 *
 * @author wangjie
 * @date 14:21 2022年07月26日
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_CODE = "errCode";

    private static final String ERROR_MSG = "errMsg";

    @ExceptionHandler(Exception.class)
    public R<Map<String, Object>> doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception ex) {
        Map<String, Object> responseData = new HashMap<>(16);
        LogUtils.logToFile(ex);
        if (ex instanceof CommonException) {
            CommonException commonException = (CommonException) ex;
            responseData.put(ERROR_CODE, commonException.getErrCode());
            responseData.put(ERROR_MSG, commonException.getErrMsg());
        } else if (ex instanceof MethodArgumentNotValidException) {
            responseData.put(ERROR_CODE, "-1");
            responseData.put(ERROR_MSG, ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        } else if (ex instanceof ConstraintViolationException) {
            responseData.put(ERROR_CODE, "-1");
            responseData.put(ERROR_MSG, ((ConstraintViolationException) ex).getConstraintViolations().iterator().next().getMessageTemplate());
        } else {
            responseData.put(ERROR_CODE, EmError.UNKNOWN_ERROR.getErrCode());
            responseData.put(ERROR_MSG, EmError.UNKNOWN_ERROR.getErrMsg());
        }

        return R.error(responseData);
    }
}
