package com.summerelvn.saga.config;

import com.summerelvn.saga.model.entities.PaymentEntity;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PaymentRequestMapper {

    @Mapping(target = "status", expression = "java(com.summerelvn.saga.model.PaymentStatus.SUCCESS)")
    PaymentEntity toDBEntity(PaymentRequest request);
    PaymentResponse fromDBEntity(PaymentEntity entity);

    PaymentRequest toPaymentRequest(OrderResponse response);

    PaymentRequest toPaymentRequest(PaymentResponse response);

    PaymentRequest toPaymentRequest(ShipmentResponse response);
}
