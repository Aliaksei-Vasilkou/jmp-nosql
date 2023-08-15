package com.epam.jmp.nosql.rateservice.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import com.epam.jmp.nosql.rateservice.model.RateLimitRule;
import com.epam.jmp.nosql.rateservice.utils.YamlPropertySourceFactory;

import jakarta.validation.Valid;

@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:rateLimitRules.yml", factory = YamlPropertySourceFactory.class)
@Validated
public class RateLimitRulesConfig {

    @Valid
    private List<RateLimitRule> rules = new ArrayList<>();

    public List<RateLimitRule> getRules() {
        return rules;
    }

    public void setRules(List<RateLimitRule> rules) {
        this.rules = rules;
    }
}
