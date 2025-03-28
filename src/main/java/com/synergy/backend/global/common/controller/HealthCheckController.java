package com.synergy.backend.global.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/v1/normal/test")
    public String healthCheck(){
        return "Health Check";
    }
}
