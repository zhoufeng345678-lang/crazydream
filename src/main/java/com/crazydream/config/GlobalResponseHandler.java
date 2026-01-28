package com.crazydream.config;

import com.crazydream.utils.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器
 * 统一处理所有API响应，确保返回格式一致
 * 
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    
    /**
     * 判断是否需要处理响应
     * 排除ResponseResult类型，避免重复包装
     * 
     * @param returnType 方法返回类型
     * @param converterType 消息转换器类型
     * @return 是否需要处理
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查是否是异常处理器的返回值
        if (returnType.getMethod().getDeclaringClass().isAnnotationPresent(RestControllerAdvice.class)) {
            return false;
        }
        
        // 获取方法的返回类型
        Class<?> returnClass = returnType.getMethod().getReturnType();
        
        // 检查是否为泛型类型
        if (returnType.getMethod().getGenericReturnType() instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType parameterizedType = (java.lang.reflect.ParameterizedType) returnType.getMethod().getGenericReturnType();
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            
            // 排除泛型的ResponseResult类型
            if (rawType.equals(ResponseResult.class)) {
                return false;
            }
        }
        
        // 排除ResponseResult类型
        if (returnClass.equals(ResponseResult.class)) {
            return false;
        }
        
        // 排除ResponseEntity类型（包括所有泛型变体）
        if (returnClass.equals(ResponseEntity.class) || returnClass.getSimpleName().startsWith("ResponseEntity")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 处理响应，包装成统一格式
     * 
     * @param body 原始响应体
     * @param returnType 方法返回类型
     * @param selectedContentType 选择的内容类型
     * @param selectedConverterType 选择的消息转换器类型
     * @param request 请求对象
     * @param response 响应对象
     * @return 包装后的响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 检查body是否已经是ResponseResult类型，避免重复包装
        if (body instanceof ResponseResult) {
            return body;
        }
        
        // 包装成统一格式
        return ResponseResult.success(body);
    }
}