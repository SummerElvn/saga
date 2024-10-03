package com.summerelvn.saga.service;

import com.summerelvn.saga.config.ConfigProps;
import com.summerelvn.saga.config.CreateOrderRequestMapper;
import com.summerelvn.saga.config.PaymentRequestMapper;
import com.summerelvn.saga.config.ShipmentRequestMapper;
import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentRequest;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.summerelvn.saga.config.Labels.*;
import static com.summerelvn.saga.util.UUIDGenerator.generateUUID;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class SagaOrchestratorService {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigProps configProps;

    CreateOrderRequestMapper orderRequestMapper = Mappers.getMapper(CreateOrderRequestMapper.class);
    PaymentRequestMapper paymentRequestMapper = Mappers.getMapper(PaymentRequestMapper.class);
    ShipmentRequestMapper shipmentRequestMapper = Mappers.getMapper(ShipmentRequestMapper.class);
    public SagaOrchestratorService(RabbitTemplate rabbitTemplate,ConfigProps configProps) {
        this.rabbitTemplate = rabbitTemplate;
        this.configProps = configProps;
    }

    public void startSaga(OrderRequest orderRequest) {
        log.info(SAGA_TRANSACTION_STARTED, orderRequest);
        if(isEmpty(orderRequest.getId()))orderRequest.setId(generateUUID());
        // Publishing event to process the order
        rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                configProps.getOrderService().getQueues().getProcessQueueCreateRoute(), orderRequest);
    }

   @RabbitListener(queues =  "#{@configProps.getOrderService().getQueues().getStatusQueueName()}")
    public void onOrderCreated(OrderResponse response, @Header("amqp_receivedRoutingKey") String routingKey) {
       log.info(ORDER_STATUS_EVENT_RECEIVED,response,routingKey);
       if (routingKey.equals(configProps.getOrderService().getQueues().getStatusQueueSuccessRoute())) {
           PaymentRequest paymentRequest = paymentRequestMapper.toPaymentRequest(response);
           // Publishing event to process the payment
           rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                   configProps.getPaymentService().getQueues().getProcessQueueCreateRoute(),
                   paymentRequest);
       } else if (routingKey.equals(configProps.getOrderService().getQueues().getStatusQueueFailureRoute())) {
           // Cancel The Order
           OrderRequest orderRequest = orderRequestMapper.toOrderRequest(response);
           rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                   configProps.getOrderService().getQueues().getProcessQueueCompensateRoute(),
                   orderRequest);
       }
    }

    @RabbitListener(queues = "#{@configProps.getPaymentService().getQueues().getStatusQueueName()}")
    public void onPaymentProcessed(PaymentResponse response, @Header("amqp_receivedRoutingKey") String routingKey) {
        log.info(PAYMENT_STATUS_EVENT_RECEIVED,response,routingKey);

        // Publishing event to process the shipping
        if (routingKey.equals(configProps.getPaymentService().getQueues().getStatusQueueSuccessRoute())) {
            ShipmentRequest request = shipmentRequestMapper.toShipmentRequest(response);
            // Publishing event to process the shipment
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getShippingService().getQueues().getProcessQueueCreateRoute(),
                    request);
        }
        //Compensation Transactions in the Payment Failure Event
        else if (routingKey.equals(configProps.getPaymentService().getQueues().getStatusQueueFailureRoute())) {

            // Cancel The Payment
            PaymentRequest paymentRequest = paymentRequestMapper.toPaymentRequest(response);
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getPaymentService().getQueues().getProcessQueueCompensateRoute(),
                    paymentRequest);

            // Cancel The Order
            OrderRequest orderRequest = orderRequestMapper.toOrderRequest(response);
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getOrderService().getQueues().getProcessQueueCompensateRoute(),
                    orderRequest);
        }
    }

    @RabbitListener(queues = "#{@configProps.getShippingService().getQueues().getStatusQueueName()}")
    public void onShippingProcessed(ShipmentResponse response, @Header("amqp_receivedRoutingKey") String routingKey) {
        if (routingKey.equals(configProps.getShippingService().getQueues().getStatusQueueSuccessRoute())) {
            log.info(SHIPPING_SUCCESS_EVENT_RECEIVED,response,routingKey);
            log.info(SAGA_TRANSACTION_SUCCESS,response);
        } //Compensation Transactions
        else if(routingKey.equals(configProps.getShippingService().getQueues().getStatusQueueFailureRoute())){

            // Cancel Shipment
            ShipmentRequest request = shipmentRequestMapper.toShipmentRequest(response);
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getShippingService().getQueues().getProcessQueueCompensateRoute(),
                    request);

            // Cancel Payment
            PaymentRequest paymentRequest = paymentRequestMapper.toPaymentRequest(response);
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getPaymentService().getQueues().getProcessQueueCompensateRoute(),
                    paymentRequest);

            // Cancel The Order
            OrderRequest orderRequest = orderRequestMapper.toOrderRequest(response);
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getOrderService().getQueues().getProcessQueueCompensateRoute(),
                    orderRequest);
        }

    }



}
