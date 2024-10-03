package com.summerelvn.saga.queue;

import com.summerelvn.saga.config.ConfigProps;
import com.summerelvn.saga.exception.MockException;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.shipping.ShipmentRequest;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import com.summerelvn.saga.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.summerelvn.saga.config.Labels.*;
import static com.summerelvn.saga.model.OrderStatus.FAILURE;

@Service
@Slf4j
public class SagaOrchestratorWorkerShippingService {

    private final ShippingService shippingService;
    private final RabbitTemplate rabbitTemplate;

    private final ConfigProps configProps;
    public SagaOrchestratorWorkerShippingService( RabbitTemplate rabbitTemplate, ConfigProps configProps,
    ShippingService shippingService){
        this.rabbitTemplate = rabbitTemplate;
        this.configProps = configProps;
        this.shippingService = shippingService;

    }
    @RabbitListener(queues =  "#{@configProps.getShippingService().getQueues().getProcessQueueName()}")
    public void processShipping( ShipmentRequest request, @Header("amqp_receivedRoutingKey") String routingKey) {
        log.info(SHIPPING_REQUEST_RECEIVED,request,routingKey);

        if (routingKey.equals(configProps.getShippingService().getQueues().getProcessQueueCreateRoute())) {
            try {
                ShipmentResponse response = shippingService.processShipment(request);
                mockException(request);
                log.info(SHIPPING_REQUEST_SUCCESS,response);
                //Publishing Success Event to Success Queue
                rabbitTemplate.convertAndSend(configProps.getQueueExchange(), //Exchange Name
                        configProps.getShippingService()
                                .getQueues()
                                .getStatusQueueSuccessRoute(), //Routing Key
                        response); //Message

            } catch (Exception e) {
                log.error(SHIPPING_REQUEST_FAILURE,request,e);
                //Publishing Failure Event to Failure Queue
                rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                        configProps.getShippingService().getQueues().getStatusQueueFailureRoute(),
                        ShipmentResponse.builder().id(request.getId()).status(FAILURE.toString()).build());
            }
        }else if(routingKey.equals(configProps.getShippingService().getQueues().getProcessQueueCompensateRoute())){
            try{
                shippingService.cancelShipment(request.getId());
            } catch (Exception e) {
                log.error(SHIPPING_COMPENSATION_FAILURE, request,e);
            }
        }
    }
    private static void mockException(ShipmentRequest request) throws MockException {
        if("shippingfailure".equalsIgnoreCase(request.getName()))
            throw new MockException("Just Mocking For Compensation Event Trigger");
    }
}
