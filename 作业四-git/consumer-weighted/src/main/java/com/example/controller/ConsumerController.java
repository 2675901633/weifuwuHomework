package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consume")
    public String consume() {
        // 使用服务名调用，负载均衡器会自动选择实例
        return "权重消费者获取信息: " + restTemplate.getForObject("http://provider-service/info", String.class);
    }
}