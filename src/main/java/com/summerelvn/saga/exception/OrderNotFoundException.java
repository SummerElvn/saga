package com.summerelvn.saga.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String msg){
        super(msg);
    }
}
