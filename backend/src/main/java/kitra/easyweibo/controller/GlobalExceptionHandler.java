package kitra.easyweibo.controller;

import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.exception.ApiException;
import kitra.easyweibo.util.ApiUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    /**
     * 自定义API异常的处理器
     */
    @ExceptionHandler(ApiException.class)
    public ApiResponse<Void> handleApiException(ApiException e) {
        return ApiUtil.failedResponse(e.getCode(), e.getMessage());
    }

    /**
     * 其他异常的处理器
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        logger.error("系统发生异常：", e);
        return ApiUtil.failedResponse(5000, "系统繁忙，请稍后再试");
    }
}
