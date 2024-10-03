package com.summerelvn.saga.service;

import com.summerelvn.saga.config.PaymentRequestMapper;
import com.summerelvn.saga.config.ShipmentRequestMapper;
import com.summerelvn.saga.exception.OrderAlreadyCancelledException;
import com.summerelvn.saga.exception.OrderNotFoundException;
import com.summerelvn.saga.model.OrderStatus;
import com.summerelvn.saga.model.PaymentStatus;
import com.summerelvn.saga.model.ShipmentStatus;
import com.summerelvn.saga.model.entities.PaymentEntity;
import com.summerelvn.saga.model.entities.ShipmentEntity;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentRequest;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import com.summerelvn.saga.repository.PaymentServiceRepo;
import com.summerelvn.saga.repository.ShippingServiceRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.summerelvn.saga.config.Labels.PAYMENT_NOT_FOUND;
import static com.summerelvn.saga.util.UUIDGenerator.generateUUID;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class ShippingService {

    private final ShippingServiceRepo shippingServiceRepo;
    ShipmentRequestMapper mapper = Mappers.getMapper(ShipmentRequestMapper.class);
    public ShippingService(ShippingServiceRepo shippingServiceRepo){
        this.shippingServiceRepo = shippingServiceRepo;
    }

    public ShipmentResponse processShipment(ShipmentRequest shipmentRequest) {
        ShipmentEntity entity = mapper.toDBEntity(shipmentRequest);
        if(isEmpty(shipmentRequest.getId())){ // this method is used for saga orchestrator as well which passes the id
            entity.setId(generateUUID());
        }
        ShipmentEntity processedShipment = shippingServiceRepo.save(entity);
        return mapper.fromDBEntity(processedShipment);
    }

    public ShipmentResponse getShipment(String id) {
            Optional<ShipmentEntity> payment =  shippingServiceRepo.findById(id);
            return payment.map(mapper::fromDBEntity)
                    .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));

    }

    public List<ShipmentResponse> getAllShipments() {
      return shippingServiceRepo.findAll().stream().map(mapper::fromDBEntity).toList();
    }

    public ShipmentResponse cancelShipment(String id) {
        return shippingServiceRepo.findById(id)
                .map(entity -> {
                    if (entity.getStatus() == ShipmentStatus.CANCELLED) {
                        throw new OrderAlreadyCancelledException
                                (String.format("The shipment id: %s is already cancelled", entity.getId()));
                    }

                    entity.setStatus(ShipmentStatus.CANCELLED);
                    shippingServiceRepo.save(entity);
                        return entity;
                    })
                .map(mapper::fromDBEntity)
                .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));
    }
    public ShipmentResponse deleteShipment(String id) {
        return shippingServiceRepo.findById(id)//existsById() - a lightweight check, just used findById to return response
                .map(order-> {
                    shippingServiceRepo.delete(order);
                    return order;
                })
                .map(deletedShipment->{
                    ShipmentResponse shipment = mapper.fromDBEntity(deletedShipment);
                    shipment.setStatus(OrderStatus.DELETED.toString());
                    return shipment;
                })
                .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));
    }

}
