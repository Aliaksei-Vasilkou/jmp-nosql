package com.epam.jmp.nosql.rateservice.service;

import java.util.Set;

import com.epam.jmp.nosql.rateservice.model.RequestDescriptor;

public interface RateLimitService {

    boolean shouldLimit(Set<RequestDescriptor> requestDescriptors);
}
