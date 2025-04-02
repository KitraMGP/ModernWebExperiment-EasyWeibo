package kitra.easyweibo.controller;

import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.exception.ApiException;
import kitra.easyweibo.exception.DatabaseOperationException;
import kitra.easyweibo.util.ApiUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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
     * 数据库访问异常的处理器
     */
    @ExceptionHandler(DatabaseOperationException.class)
    public ApiResponse<Void> handleDatabaseOperationException(DatabaseOperationException e) {
        logger.error("数据库访问异常！", e);
        return ApiUtil.failedResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ApiUtil.failedResponse(4100, "请求格式有误");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ApiUtil.failedResponse(4100, "请求格式有误");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ApiUtil.failedResponse(4100, "请求格式有误");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ApiResponse<Void> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return ApiUtil.failedResponse(4100, "请求格式有误");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ApiUtil.failedResponse(4201, "文件长度超出限制");
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
