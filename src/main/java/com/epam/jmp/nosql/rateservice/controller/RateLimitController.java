package com.epam.jmp.nosql.rateservice.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.jmp.nosql.rateservice.model.RequestDescriptor;
import com.epam.jmp.nosql.rateservice.service.RateLimitService;

@RestController
@RequestMapping("/api/v1/ratelimit")
public class RateLimitController {

    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @PostMapping
    public ResponseEntity<Void> shouldRateLimit(@RequestBody Set<RequestDescriptor> requestDescriptors) {

        if (rateLimitService.shouldLimit(requestDescriptors)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        return ResponseEntity.ok().build();
    }
}
