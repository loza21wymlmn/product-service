package com.example.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> checkHealth() {
        return Map.of(
                "service", "product-service",
                "version", "1.0.0",
                "status", "UP",
                "timestamp", Instant.now()
        );
    }
}
