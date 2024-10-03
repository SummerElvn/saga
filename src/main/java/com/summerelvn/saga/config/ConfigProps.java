package com.summerelvn.saga.config;

import com.summerelvn.saga.model.config.ServiceProps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ConfigProps {
    private String queueExchange;
    private ServiceProps paymentService;
    private ServiceProps orderService;
    private ServiceProps shippingService;
}
