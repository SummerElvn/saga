package com.summerelvn.saga.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@Slf4j
public class CustomErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        // Log the error
        log.error("Error occurred in RabbitMQ listener: ", t);

        // Suppress the exception
    }
}
