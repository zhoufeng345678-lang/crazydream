package com.crazydream.controller;

import com.crazydream.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseResult<String> healthCheck() {
        return ResponseResult.success("API服务正常运行");
    }
}
