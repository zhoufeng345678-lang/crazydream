package com.crazydream.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 统一响应结果类
 * 用于封装API的响应结果
 * 
 * @author CrazyDream Team
 * @since 2025-12-09
 * @param <T> 响应数据类型
 */
public class ResponseResult<T> {
    
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 私有构造函数
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    private ResponseResult(int code, String message, T data) {
        if (data instanceof ResponseResult) {
            throw new IllegalArgumentException("Data cannot be a ResponseResult to avoid nesting");
        }
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应
     * @param data 响应数据
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "成功", data);
    }
    
    /**
     * 成功响应（无数据）
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(200, "成功", null);
    }
    
    /**
     * 错误响应
     * @param code 错误码
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }
    
    /**
     * 错误响应（无消息）
     * @param code 错误码
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(int code) {
        return new ResponseResult<>(code, "错误", null);
    }

    /**
     * 错误响应（默认500）
     * @param message 错误消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message, null);
    }
    
    /**
     * 创建带HTTP状态码的响应实体
     * @param responseResult 响应结果
     * @return 响应实体
     */
    public static <T> ResponseEntity<ResponseResult<T>> createResponseEntity(ResponseResult<T> responseResult) {
        HttpStatus httpStatus = HttpStatus.OK;
        if (responseResult.getCode() >= 400) {
            switch (responseResult.getCode()) {
                case 400:
                    httpStatus = HttpStatus.BAD_REQUEST;
                    break;
                case 401:
                    httpStatus = HttpStatus.UNAUTHORIZED;
                    break;
                case 403:
                    httpStatus = HttpStatus.FORBIDDEN;
                    break;
                case 404:
                    httpStatus = HttpStatus.NOT_FOUND;
                    break;
                case 500:
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                    break;
                default:
                    httpStatus = HttpStatus.BAD_REQUEST;
            }
        }
        return new ResponseEntity<>(responseResult, httpStatus);
    }
    
    // getter和setter方法
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    

}
