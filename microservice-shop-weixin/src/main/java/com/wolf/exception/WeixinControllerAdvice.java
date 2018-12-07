package com.wolf.exception;

import com.wolf.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class WeixinControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Result errorHandler(Exception e) {
        log.error("#####全局异常处理，系统异常：{}", e.getMessage());
        return Result.ERROR(e.getMessage());
    }

    @ExceptionHandler(WeixinException.class)
    public Result memberErrorHandler(WeixinException e) {
        log.error("#####全局异常处理，业务异常：{}", e.getMessage());
        return new Result(e.getCode(), e.getMsg(), null);
    }
}