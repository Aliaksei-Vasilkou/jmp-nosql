package com.epam.jmp.nosql.rateservice.model;

import java.time.temporal.ChronoUnit;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class RateLimitRule {

    private String accountId;

    private String clientIp;

    private String requestType;

    @Min(value = 1)
    private int allowedNumberOfRequests;

    @NotNull
    private ChronoUnit timeInterval;

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = ChronoUnit.valueOf(timeInterval);
    }
}
