package com.crazydream.common;

import lombok.Data;

/**
 * API统一响应格式
 * 封装所有API的响应结果，包含状态码、消息和数据
 * 
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@Data
public class ApiResponse<T> {
    
    /**
     * 状态码
     * 200表示成功，其他表示失败
     */
    private Integer code;
    
    /**
     * 响应消息
     * 描述操作结果
     */
    private String message;
    
    /**
     * 响应数据
     * 实际的业务数据
     */
    private T data;
    
    /**
     * 私有构造方法，防止直接实例化
     */
    private ApiResponse() {
    }
    
    /**
     * 成功响应，不带数据
     * 
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        return response;
    }
    
    /**
     * 成功响应，带数据
     * 
     * @param <T> 数据类型
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(data);
        return response;
    }
    
    /**
     * 成功响应，带数据和自定义消息
     * 
     * @param <T> 数据类型
     * @param data 响应数据
     * @param message 自定义消息
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    /**
     * 失败响应，指定状态码和消息
     * 
     * @param <T> 数据类型
     * @param code 状态码
     * @param message 错误消息
     * @return 失败响应对象
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 失败响应，使用默认状态码500
     * 
     * @param <T> 数据类型
     * @param message 错误消息
     * @return 失败响应对象
     */
    public static <T> ApiResponse<T> fail(String message) {
        return fail(500, message);
    }
}