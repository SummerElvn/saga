package com.summerelvn.saga.config.queue;

import com.summerelvn.saga.config.ConfigProps;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final ConfigProps configProps;
    public RabbitMQConfig(ConfigProps configProps){
        this.configProps = configProps;
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configure the RabbitTemplate to use the Jackson message converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());  // Set the converter
        return rabbitTemplate;
    }


    @Bean
    public Exchange sagaExchange() {
        return ExchangeBuilder
                .topicExchange(configProps.getQueueExchange())
                .durable(true) //exchange or queue  will survive a broker/rabbit server restart
                .build();
    }

    // ORDER QUEUE BINDINGS
            @Bean
            public Queue orderProcessQueue() {
                return new Queue(configProps.getOrderService().getQueues().getProcessQueueName(), true);
            }

            @Bean
            public Binding bindingOrderQueueWithCreateRoute() {
                return BindingBuilder
                        .bind(orderProcessQueue())
                        .to(sagaExchange())
                        .with(configProps.getOrderService()
                                .getQueues()
                                .getProcessQueueCreateRoute())
                        .noargs();
            }

            @Bean
            public Binding bindingOrderQueueWithCompensateRoute() {
                return BindingBuilder
                        .bind(orderProcessQueue())
                        .to(sagaExchange())
                        .with(configProps.getOrderService()
                                .getQueues()
                                .getProcessQueueCompensateRoute())
                        .noargs();
            }

    // ORDER STATUS QUEUE BINDINGS

        @Bean
        public Queue orderStatusQueue() {
            return new Queue(configProps.getOrderService().getQueues().getStatusQueueName(), true);
        }
        @Bean
        public Binding bindingOrderQueueWithSuccessRoute() {
            return BindingBuilder
                    .bind(orderStatusQueue()) //with Queue
                    .to(sagaExchange()) //With Exchange
                    .with(configProps.getOrderService().getQueues().getStatusQueueSuccessRoute()) //with Route
                    .noargs();
        }
        @Bean
        public Binding bindingOrderQueueWithFailureRoute() {
            return BindingBuilder
                    .bind(orderStatusQueue()) //with Queue
                    .to(sagaExchange()) //With Exchange
                    .with(configProps.getOrderService().getQueues().getStatusQueueFailureRoute()) //with Route
                    .noargs();
        }

    // PAYMENT QUEUE BINDINGS
            @Bean
            public Queue paymentProcessQueue() {
                return new Queue(configProps.getPaymentService().getQueues().getProcessQueueName(), true);
            }

            @Bean
            public Binding bindingPaymentQueueCreateRoute() {
                return BindingBuilder
                        .bind(paymentProcessQueue())
                        .to(sagaExchange())
                        .with(configProps.getPaymentService()
                                .getQueues()
                                .getProcessQueueCreateRoute())
                        .noargs();
            }

            @Bean
            public Binding bindingPaymentQueueCompensateRoute() {
                return BindingBuilder
                        .bind(paymentProcessQueue())
                        .to(sagaExchange())
                        .with(configProps.getPaymentService()
                                .getQueues()
                                .getProcessQueueCompensateRoute())
                        .noargs();
    }

    // PAYMENT STATUS QUEUE BINDINGS
            @Bean
            public Queue paymentSuccessQueue() {
                return new Queue(configProps.getPaymentService().getQueues().getStatusQueueName(), true);
            }
            @Bean
            public Binding bindingPaymentStatusQueueSuccessRoute() {
                return BindingBuilder
                        .bind(paymentSuccessQueue()) //with Queue
                        .to(sagaExchange()) //With Exchange
                        .with(configProps.getPaymentService().getQueues().getStatusQueueSuccessRoute()) //with Route
                        .noargs();
            }
            @Bean
            public Binding bindingPaymentStatusQueueFailureRoute() {
                return BindingBuilder
                        .bind(paymentSuccessQueue()) //with Queue
                        .to(sagaExchange()) //With Exchange
                        .with(configProps.getPaymentService().getQueues().getStatusQueueFailureRoute()) //with Route
                        .noargs();
            }

    // SHIPPING QUEUE BINDINGS
        @Bean
        public Queue shippingProcessQueue() {
            return new Queue(configProps.getShippingService().getQueues().getProcessQueueName(), true);
        }

        @Bean
        public Binding bindingShippingQueueWithCreateRoute() {
            return BindingBuilder
                    .bind(shippingProcessQueue())
                    .to(sagaExchange())
                    .with(configProps.getShippingService()
                            .getQueues()
                            .getProcessQueueCreateRoute())
                    .noargs();
        }

        @Bean
        public Binding bindingShippingQueueWithCompensateRoute() {
            return BindingBuilder
                    .bind(shippingProcessQueue())
                    .to(sagaExchange())
                    .with(configProps.getShippingService()
                            .getQueues()
                            .getProcessQueueCompensateRoute())
                    .noargs();
        }

    // SHIPPING Status QUEUE BINDINGS
        @Bean
        public Queue shippingSuccessQueue() {
            return new Queue(configProps.getShippingService().getQueues().getStatusQueueName(), true);
        }
        @Bean
        public Binding bindingShippingStatusQueueWithSuccessRoute() {
            return BindingBuilder
                    .bind(shippingSuccessQueue()) //with Queue
                    .to(sagaExchange()) //With Exchange
                    .with(configProps.getShippingService().getQueues().getStatusQueueSuccessRoute()) //with Route
                    .noargs();
        }
        @Bean
        public Binding bindingShippingStatusQueueWithFailureRoute() {
            return BindingBuilder
                    .bind(shippingSuccessQueue()) //with Queue
                    .to(sagaExchange()) //With Exchange
                    .with(configProps.getShippingService().getQueues().getStatusQueueFailureRoute()) //with Route
                    .noargs();
        }
}
