package com.summerelvn.saga.controller;

import com.summerelvn.saga.exception.ErrorResponse;
import com.summerelvn.saga.exception.OrderAlreadyCancelledException;
import com.summerelvn.saga.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static com.summerelvn.saga.config.Labels.*;
import static com.summerelvn.saga.config.Labels.ORDER_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(OrderAlreadyCancelledException.class)
    public ResponseEntity<ErrorResponse> orderAlreadyCancelledException(OrderAlreadyCancelledException ex){
        log.error(EXCEPTION_OCCURED,ex);
        return new ResponseEntity<>(buildErrorResponse(CANCELLED_EXCEPTION,ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderNotFoundException(OrderNotFoundException ex){
        log.error(EXCEPTION_OCCURED,ex);
        return new ResponseEntity<>(buildErrorResponse(NOT_FOUND,ORDER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex){
        log.error(EXCEPTION_OCCURED,ex);

        return new ResponseEntity<>(buildErrorResponse(BAD_REQUEST,INVALID_ORDER), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> otherExceptions(Exception ex){
        log.error(EXCEPTION_OCCURED,ex);
        return new ResponseEntity<>(Map.of("Exception",ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorResponse buildErrorResponse(String errorTitle, String errorMsg) {
        return ErrorResponse.builder()
                .title(errorTitle)
                .message(errorMsg)
                .build();
    }
}
