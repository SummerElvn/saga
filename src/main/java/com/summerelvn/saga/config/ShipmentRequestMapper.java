package com.summerelvn.saga.config;

import com.summerelvn.saga.model.entities.PaymentEntity;
import com.summerelvn.saga.model.entities.ShipmentEntity;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentRequest;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ShipmentRequestMapper {

    @Mapping(target = "status", expression = "java(com.summerelvn.saga.model.ShipmentStatus.SUCCESS)")
    ShipmentEntity toDBEntity(ShipmentRequest request);
    ShipmentResponse fromDBEntity(ShipmentEntity entity);

    ShipmentRequest toShipmentRequest(PaymentResponse response);

    ShipmentRequest toShipmentRequest(ShipmentResponse response);
}
