package com.summerelvn.saga.config;

import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.model.entities.OrderEntity;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CreateOrderRequestMapper {

    @Mapping(target = "status", expression = "java(com.summerelvn.saga.model.OrderStatus.CREATED)")
    OrderEntity toDBEntity(OrderRequest request);
    OrderResponse fromDBEntity(OrderEntity entity);

    OrderRequest toOrderRequest(OrderResponse response);

    OrderRequest toOrderRequest(PaymentResponse response);

    OrderRequest toOrderRequest(ShipmentResponse response);



}
