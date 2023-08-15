package com.epam.jmp.nosql.rateservice.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.epam.jmp.nosql.rateservice.model.RateLimitRule;
import com.epam.jmp.nosql.rateservice.model.RequestDescriptor;

import lombok.extern.log4j.Log4j2;

import redis.clients.jedis.Jedis;

@Log4j2
@Component
public class JedisRateLimitService implements RateLimitService {

    private final Jedis jedis;
    private final RuleService ruleService;

    public JedisRateLimitService(Jedis jedis, RuleService ruleService) {
        this.jedis = jedis;
        this.ruleService = ruleService;
    }

    @Override
    public boolean shouldLimit(Set<RequestDescriptor> requestDescriptors) {
        Map<String, RateLimitRule> rulesToApply = ruleService.getRulesToApply(requestDescriptors);
        log.info("Found rules to apply: {}", rulesToApply.size());

        for (Map.Entry<String, RateLimitRule> ruleEntry : rulesToApply.entrySet()) {
            boolean result = checkAvailability(ruleEntry.getKey(), ruleEntry.getValue());

            if (result) {
                return true;
            }
        }

        return false;
    }

    private boolean checkAvailability(String key, RateLimitRule rule) {
        String value = jedis.get(key);
        int limit = rule.getAllowedNumberOfRequests();

        if (value != null) {
            int counter = Integer.parseInt(value);
            log.info("Current value for key '{}' is {} from {}", key, counter, limit);
            if (counter >= limit) {
                return true;
            }
            else {
                jedis.incr(key);
            }
        }
        else {
            log.info("Caching new value for '{}' key ", key);
            jedis.setex(key, getExpirationTime(rule), "1");
        }

        return false;
    }

    private Long getExpirationTime(RateLimitRule rule) {
        return rule.getTimeInterval().getDuration().getSeconds();
    }
}
