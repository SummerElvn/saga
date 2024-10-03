package com.summerelvn.saga.config;

public class Labels {

    public static final String NOT_FOUND = "NOT FOUND EXCEPTION";
    public static final String UNKNOWN_EXCEPTION = "UNKNOWN EXCEPTION";
    public static final String CANCELLED_EXCEPTION = "CANCELLED EXCEPTION";

    public static final String BAD_REQUEST = "Bad Request";
    public static final String INVALID_ORDER = "Invalid Order Id";
    public static final String ORDER_NOT_FOUND = "Order Not Found";

    public static final String PAYMENT_NOT_FOUND = "Payment Not Found";

    public static final String INTERNAL_SERVICE_FAILURE = "Internal Service Failure";
    public static final String EXCEPTION_OCCURED ="Exception Occurred";

    public static final String ORDER_RECEIVED = "Order Request Received in Order-Process-Queue: {} with RoutingKey: {}";
    public static final String ORDER_SUCCESS ="Order Successful, Publishing Success Event to | Order-Status-Queue | {}";
    public static final String ORDER_FAILURE ="Order Failed, Publishing Failure Event to | Order-Status-Queue | {}";
    public static final String ORDER_COMPENSATION_FAILURE ="Order Compensation Failed, in | Order-Process-Queue | {}";

    public static final String SHIPPING_REQUEST_RECEIVED = "Shipping Request Received from | Shipping-Process-Queue |: {} with RoutingKey: {}";
    public static final String SHIPPING_REQUEST_SUCCESS ="Shipping Successful, Publishing Success Event to | Shipping-Status-Queue | {}";
    public static final String SHIPPING_REQUEST_FAILURE ="Shipping Failed, Publishing Failure Event to | Shipping-Status-Queue|  {}";

    public static final String SHIPPING_COMPENSATION_FAILURE ="Shipping Compensation Failed, in | Shipping-Process-Queue|  {}";

    public static final String PAYMENT_REQUEST_RECEIVED = "Payment Request Received from | Payment-Process-Queue |: {} with RoutingKey: {}";
    public static final String PAYMENT_REQUEST_SUCCESS ="Payment Successful, Publishing Success Event to | Payment-Status-Queue | {}";
    public static final String PAYMENT_REQUEST_FAILURE ="Payment Failed, Publishing Failure Event to | Payment-Status-Queue | {}";

    public static final String PAYMENT_COMPENSATION_FAILURE ="Payment Compensation Failed in | Payment-Process-Queue | {}";

    public static final String ORDER_STATUS_EVENT_RECEIVED ="Order Status Event Received from | Order-Status-Queue | {} with RoutingKey: {}";
    public static final String PAYMENT_STATUS_EVENT_RECEIVED ="Payment Status Event Received from | Payment-Status-Queue | {} with RoutingKey: {}";

    public static final String SHIPPING_SUCCESS_EVENT_RECEIVED ="Successful Shipping Event Received from | Shipping-Status-Queue | {} with RoutingKey: {}";

    public static final String SAGA_TRANSACTION_STARTED ="Saga Transaction Started {}";
    public static final String SAGA_TRANSACTION_SUCCESS ="Successfully Completed Saga Transaction {}";

}
