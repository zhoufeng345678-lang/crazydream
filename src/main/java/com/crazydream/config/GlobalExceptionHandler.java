package com.crazydream.config;

import com.crazydream.utils.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * 处理参数验证异常 (@Valid/@Validated注解验证失败)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warning("参数验证失败: " + errorMessage);
        return ResponseResult.error(400, errorMessage);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<String> handleBindException(BindException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.warning("参数绑定失败: " + errorMessage);
        return ResponseResult.error(400, errorMessage);
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public ResponseResult<String> handleSQLException(SQLException e) {
        logger.severe("SQL异常: " + e.getMessage());
        return ResponseResult.error(500, "数据库操作失败，请稍后重试");
    }

    /**
     * 处理资源未找到异常 (Spring Boot 3.2+)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseResult<String> handleNoResourceFoundException(NoResourceFoundException e) {
        logger.warning("资源未找到: " + e.getMessage());
        return ResponseResult.error(404, "请求的资源不存在: " + e.getResourcePath());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResult<String> handleNotFoundException(NoHandlerFoundException e) {
        logger.warning("资源未找到: " + e.getMessage());
        return ResponseResult.error(404, "请求的资源不存在");
    }

    /**
     * 处理业务异常 - 资源不存在
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<String> handleRuntimeException(RuntimeException e) {
        logger.warning("业务异常: " + e.getMessage());
        if ("分类不存在".equals(e.getMessage()) || "子目标不存在".equals(e.getMessage()) || "用户不存在".equals(e.getMessage())) {
            return ResponseResult.error(404, e.getMessage());
        } else if ("密码不能为空".equals(e.getMessage()) || "密码长度不能少于6位".equals(e.getMessage())) {
            return ResponseResult.error(400, e.getMessage());
        }
        return ResponseResult.error(500, "系统内部错误，请稍后重试");
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleException(Exception e) {
        logger.severe("系统异常: " + e.getMessage());
        e.printStackTrace();
        return ResponseResult.error(500, "系统内部错误，请稍后重试");
    }
}
