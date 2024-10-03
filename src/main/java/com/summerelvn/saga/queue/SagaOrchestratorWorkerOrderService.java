package com.summerelvn.saga.queue;

import com.summerelvn.saga.config.ConfigProps;
import com.summerelvn.saga.exception.MockException;
import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import static com.summerelvn.saga.config.Labels.*;
import static com.summerelvn.saga.model.OrderStatus.FAILURE;

@Service
@Slf4j
public class SagaOrchestratorWorkerOrderService {


    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    private final ConfigProps configProps;
    public SagaOrchestratorWorkerOrderService(OrderService orderService, RabbitTemplate rabbitTemplate,ConfigProps configProps){
        this.orderService = orderService ;
        this.rabbitTemplate = rabbitTemplate;
        this.configProps = configProps;

    }
    @RabbitListener(queues =  "#{@configProps.getOrderService().getQueues().getProcessQueueName()}")
    //@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processOrder(OrderRequest orderRequest, @Header("amqp_receivedRoutingKey") String routingKey) {
        log.info(ORDER_RECEIVED, orderRequest,routingKey);
        if (routingKey.equals(configProps.getOrderService().getQueues().getProcessQueueCreateRoute())) {
            try {
                OrderResponse response =  orderService.createOrder(orderRequest);
                mockException(orderRequest);
                log.info(ORDER_SUCCESS,response);
                //Publishing Success Event to Status Queue
                rabbitTemplate.convertAndSend(configProps.getQueueExchange(), //Exchange Name
                        configProps.getOrderService()
                                .getQueues()
                                .getStatusQueueSuccessRoute(), //Routing Key
                        response); //Message
            } catch (Exception e) {
                log.error(ORDER_FAILURE, orderRequest,e);
                //Publishing Failure Event to Status Queue

                    rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                            configProps.getOrderService().getQueues().getStatusQueueFailureRoute(),
                            OrderResponse.builder().id(orderRequest.getId()).status(FAILURE.toString()).build());
            }
        }else if(routingKey.equals(configProps.getOrderService().getQueues().getProcessQueueCompensateRoute())){
            try{
                orderService.cancelOrder(orderRequest.getId());
            } catch (Exception e) {
                log.error(ORDER_FAILURE, orderRequest,e);
            }
        }
    }

    private static void mockException(OrderRequest orderRequest) throws MockException {
        if("orderfailure".equalsIgnoreCase(orderRequest.getName()))
            throw new MockException("Just Mocking For Compensation Event Trigger");
    }
}
