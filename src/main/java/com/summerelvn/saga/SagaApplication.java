package com.summerelvn.saga;

import com.summerelvn.saga.config.ConfigProps;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableRabbit
public class SagaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagaApplication.class, args);
	}

}
