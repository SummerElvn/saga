package com.summerelvn.saga.queue;

import com.summerelvn.saga.config.ConfigProps;
import com.summerelvn.saga.exception.MockException;
import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.summerelvn.saga.config.Labels.*;
import static com.summerelvn.saga.model.OrderStatus.FAILURE;

@Service
@Slf4j
public class SagaOrchestratorWorkerPaymentService {
    private final RabbitTemplate rabbitTemplate;
    private final PaymentService paymentService;
    private final ConfigProps configProps;
    public SagaOrchestratorWorkerPaymentService(RabbitTemplate rabbitTemplate, ConfigProps configProps,
                                                PaymentService paymentService){
        this.rabbitTemplate = rabbitTemplate;
        this.configProps = configProps;
        this.paymentService = paymentService;

    }
    @RabbitListener(queues =  "#{@configProps.getPaymentService().getQueues().getProcessQueueName()}")
    public void processPayment(PaymentRequest paymentRequest, @Header("amqp_receivedRoutingKey") String routingKey) {
        log.info(PAYMENT_REQUEST_RECEIVED,paymentRequest,routingKey);
        if (routingKey.equals(getCreateRoute())) {
            processPayment(paymentRequest);
        } //Com
        else if(routingKey.equals(getCompensateRoute())){
            compensatePayment(paymentRequest);
        }
    }

    private String getCompensateRoute() {
        return configProps.getPaymentService().getQueues().getProcessQueueCompensateRoute();
    }

    private String getCreateRoute() {
        return configProps.getPaymentService().getQueues().getProcessQueueCreateRoute();
    }

    private void processPayment(PaymentRequest paymentRequest) {
        try {

            PaymentResponse response = paymentService.processPayment(paymentRequest);
            mockException(paymentRequest);
            log.info(PAYMENT_REQUEST_SUCCESS, paymentRequest);
            //Publishing Success Event to Success Queue
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(), //Exchange Name
                    configProps.getPaymentService()
                            .getQueues()
                            .getStatusQueueSuccessRoute(), //Routing Key
                    response); //Message

        } catch (Exception e) {
            log.error(PAYMENT_REQUEST_FAILURE, paymentRequest,e);
            //Publishing Failure Event to Failure Queue
            rabbitTemplate.convertAndSend(configProps.getQueueExchange(),
                    configProps.getPaymentService().getQueues().getStatusQueueFailureRoute(),
                    PaymentResponse.builder().id(paymentRequest.getId()).status(FAILURE.toString()).build());
        }
    }

    private void compensatePayment(PaymentRequest paymentRequest) {
        try{
            paymentService.cancelPayment(paymentRequest.getId());
        } catch (Exception e) {
            log.error(PAYMENT_COMPENSATION_FAILURE, paymentRequest,e);
        }
    }

    private static void mockException(PaymentRequest paymentRequest) throws MockException {
        if("paymentfailure".equalsIgnoreCase(paymentRequest.getName()))
            throw new MockException("Just Mocking For Compensation Event Trigger");
    }
}
