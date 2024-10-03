package com.summerelvn.saga.exception;

public class ServiceException extends RuntimeException{
    private ErrorResponse errorResponse;
    public ServiceException(String msg){
        super(msg);
    }

    public ServiceException(ErrorResponse errorResponse){
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse(){
        return errorResponse;
    }
}
