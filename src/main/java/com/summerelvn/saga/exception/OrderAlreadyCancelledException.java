package com.summerelvn.saga.exception;

public class OrderAlreadyCancelledException extends RuntimeException{
    public OrderAlreadyCancelledException(String msg){
        super(msg);
    }

}
