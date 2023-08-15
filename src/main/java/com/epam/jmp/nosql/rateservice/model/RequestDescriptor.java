package com.epam.jmp.nosql.rateservice.model;

public record RequestDescriptor(String accountId, String clientIp, String requestType) {

}
