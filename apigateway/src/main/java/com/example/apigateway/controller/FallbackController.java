package com.example.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<Map<String, Object>> userServiceFallback() {
        return Mono.just(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "User service is temporarily unavailable",
                "message", "Please try again later"
        ));
    }

    @PostMapping("/user-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<Map<String, Object>> userServicePostFallback() {
        return userServiceFallback();
    }

    @PutMapping("/user-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<Map<String, Object>> userServicePutFallback() {
        return userServiceFallback();
    }

    @DeleteMapping("/user-service")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<Map<String, Object>> userServiceDeleteFallback() {
        return userServiceFallback();
    }
}