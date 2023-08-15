package com.epam.jmp.nosql.rateservice.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import com.epam.jmp.nosql.rateservice.config.RateLimitRulesConfig;
import com.epam.jmp.nosql.rateservice.model.RateLimitRule;
import com.epam.jmp.nosql.rateservice.model.RequestDescriptor;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RuleService {

    private final RateLimitRulesConfig rateLimitRulesConfig;
    private Map<String, RateLimitRule> ruleMap;

    public Map<String, RateLimitRule> getRulesToApply(Set<RequestDescriptor> descriptors) {
        ruleMap.clear();
        List<RateLimitRule> rules = rateLimitRulesConfig.getRules();

        for (RateLimitRule rule : rules) {
            boolean flag = checkAccountIdDescriptor(rule, descriptors);
            if (flag) {
                ruleMap.put("accountId", rule);
                return ruleMap;
            }

            flag = checkClientIpDescriptor(rule, descriptors);
            if (flag) {
                ruleMap.put("clientIp", rule);
            }

            flag = checkRequestTypeDescriptor(rule, descriptors);
            if (flag) {
                ruleMap.put("requestType", rule);
            }
        }

        return ruleMap;
    }

    private boolean checkAccountIdDescriptor(RateLimitRule rule, Set<RequestDescriptor> descriptors) {
        String accountId = rule.getAccountId();

        return isNotBlank(accountId) && descriptors.stream()
                .anyMatch(it -> isNotBlank(it.accountId()) && it.accountId().equals(accountId));
    }

    private boolean checkClientIpDescriptor(RateLimitRule rule, Set<RequestDescriptor> descriptors) {
        String clientIp = rule.getClientIp();

        return isNotBlank(clientIp) && descriptors.stream()
                .anyMatch(it -> isNotBlank(it.clientIp()) && it.clientIp().equals(clientIp));
    }

    private boolean checkRequestTypeDescriptor(RateLimitRule rule, Set<RequestDescriptor> descriptors) {
        String requestType = rule.getRequestType();

        return isNotBlank(requestType) && descriptors.stream()
                .anyMatch(it -> isNotBlank(it.requestType()) && it.requestType().equals(requestType));
    }
}
