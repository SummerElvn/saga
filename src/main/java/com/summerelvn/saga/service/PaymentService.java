package com.summerelvn.saga.service;

import com.summerelvn.saga.config.PaymentRequestMapper;
import com.summerelvn.saga.exception.OrderAlreadyCancelledException;
import com.summerelvn.saga.exception.OrderNotFoundException;
import com.summerelvn.saga.model.OrderStatus;
import com.summerelvn.saga.model.PaymentStatus;
import com.summerelvn.saga.model.entities.PaymentEntity;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.repository.PaymentServiceRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.summerelvn.saga.config.Labels.PAYMENT_NOT_FOUND;
import static com.summerelvn.saga.util.UUIDGenerator.generateUUID;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class PaymentService {

    private final PaymentServiceRepo paymentServiceRepo;
    PaymentRequestMapper mapper = Mappers.getMapper(PaymentRequestMapper.class);
    public PaymentService(PaymentServiceRepo paymentServiceRepo){
        this.paymentServiceRepo = paymentServiceRepo;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        PaymentEntity entity = mapper.toDBEntity(paymentRequest);
        if(isEmpty(paymentRequest.getId())){ // this method is used for saga orchestrator as well which passes the id
            entity.setId(generateUUID());
        }
        PaymentEntity processedPayment = paymentServiceRepo.save(entity);
        return mapper.fromDBEntity(processedPayment);
    }

    public PaymentResponse getPayment(String id) {
            Optional<PaymentEntity> payment =  paymentServiceRepo.findById(id);
            return payment.map(mapper::fromDBEntity)
                    .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));

    }

    public List<PaymentResponse> getAllPayments() {
      return paymentServiceRepo.findAll().stream().map(mapper::fromDBEntity).toList();
    }

    public PaymentResponse cancelPayment(String id) {
        return paymentServiceRepo.findById(id)
                .map(orderEntity -> {
                    if (orderEntity.getStatus() == PaymentStatus.CANCELLED) {
                        throw new OrderAlreadyCancelledException
                                (String.format("The payment id: %s is already cancelled", orderEntity.getId()));
                    }else if(orderEntity.getStatus() == PaymentStatus.SUCCESS){
                        orderEntity.setStatus(PaymentStatus.REFUND_INITIATED);
                    }else {
                        orderEntity.setStatus(PaymentStatus.CANCELLED);
                    }
                        orderEntity.setStatus(PaymentStatus.CANCELLED);
                        paymentServiceRepo.save(orderEntity);
                        return orderEntity;
                    })
                .map(mapper::fromDBEntity)
                .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));
    }
    public PaymentResponse deletePayment(String id) {
        return paymentServiceRepo.findById(id)//existsById() - a lightweight check, just used findById to return response
                .map(order-> {
                    paymentServiceRepo.delete(order);
                    return order;
                })
                .map(deletedOrder->{
                    PaymentResponse payment = mapper.fromDBEntity(deletedOrder);
                    payment.setStatus(OrderStatus.DELETED.toString());
                    return payment;
                })
                .orElseThrow(() -> new OrderNotFoundException(PAYMENT_NOT_FOUND));
    }

}
