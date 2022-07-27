package com.wj.boot.exception;

import com.wj.boot.entity.response.R;
import com.wj.boot.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception ex) {
        Map<String, Object> responseData = new HashMap<>(16);
        LogUtils.logToFile(ex);
        if (ex instanceof CommonException) {
            CommonException commonException = (CommonException) ex;
            responseData.put("errCode", commonException.getErrCode());
            responseData.put("errMsg", commonException.getErrMsg());
        } else if (ex instanceof MethodArgumentNotValidException) {
            responseData.put("errCode", "-1");
            responseData.put("errMsg", ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        } else if (ex instanceof ConstraintViolationException) {
            responseData.put("errCode", "-1");
            responseData.put("errMsg", ((ConstraintViolationException) ex).getConstraintViolations().iterator().next().getMessageTemplate());
        } else {
            responseData.put("errCode", EmError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmError.UNKNOWN_ERROR.getErrMsg());
        }

        return R.error(responseData);
    }
}
