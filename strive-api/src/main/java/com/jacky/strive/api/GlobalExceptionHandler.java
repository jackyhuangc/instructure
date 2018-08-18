package com.jacky.strive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import qsq.biz.scheduler.entity.ResResult;

import java.util.Optional;

/**
 * @ControllerAdvice + @ExceptionHandler 实现全局的 Controller 层的异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常 // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 默认返回200即可
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResResult handleException(Exception e) {

        LOGGER.error(e.getMessage(), e);
        return ResResult.fail(Optional.ofNullable(e.getMessage()).orElse("UNKNOWN"));
    }
}