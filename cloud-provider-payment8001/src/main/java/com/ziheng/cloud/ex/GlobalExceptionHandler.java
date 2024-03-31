package com.ziheng.cloud.ex;

import com.ziheng.cloud.resp.ResultData;
import com.ziheng.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        log.error("全局异常处理：{}", e.getMessage(),e);
        return ResultData.fali(ReturnCodeEnum.RC500.getCode(), e.getMessage());
    }

}
