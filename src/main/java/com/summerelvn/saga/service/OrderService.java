package com.summerelvn.saga.service;

import com.summerelvn.saga.config.CreateOrderRequestMapper;
import com.summerelvn.saga.exception.MockException;
import com.summerelvn.saga.exception.OrderAlreadyCancelledException;
import com.summerelvn.saga.exception.OrderNotFoundException;
import com.summerelvn.saga.model.OrderStatus;
import com.summerelvn.saga.model.entities.OrderEntity;
import com.summerelvn.saga.model.entities.OrderStatusHistoryEntity;
import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.repository.OrderServiceRepo;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static com.summerelvn.saga.config.Labels.ORDER_NOT_FOUND;
import static com.summerelvn.saga.util.UUIDGenerator.generateUUID;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class OrderService {

    private final OrderServiceRepo orderServiceRepo;
    CreateOrderRequestMapper mapper = Mappers.getMapper(CreateOrderRequestMapper.class);
    public OrderService(OrderServiceRepo orderServiceRepo){
        this.orderServiceRepo = orderServiceRepo;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {

        OrderEntity entity = mapper.toDBEntity(orderRequest);
        if(isEmpty(entity.getId()))entity.setId(generateUUID());
        OrderStatusHistoryEntity statusHistory = OrderStatusHistoryEntity.builder()
                .order(entity)
                .status(OrderStatus.CREATED)
                .build();
        entity.setStatusHistory(List.of(statusHistory));
        OrderEntity savedOrder = orderServiceRepo.save(entity);
        return mapper.fromDBEntity(savedOrder);
    }

    public OrderResponse getOrder(String id) {
            Optional<OrderEntity> order =  orderServiceRepo.findById(id);
            return order.map(mapper::fromDBEntity)
                    .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));

    }

    public List<OrderResponse> getAllOrders() {
      return orderServiceRepo.findAll().stream().map(mapper::fromDBEntity).toList();
    }

    @Transactional
    public OrderResponse cancelOrder(String id) {
       return orderServiceRepo.findById(id)
                .map(orderEntity -> {
                    if (orderEntity.getStatus() == OrderStatus.CANCELLED) {
                        throw new OrderAlreadyCancelledException
                                (String.format("The order id: %s is already cancelled", orderEntity.getId()));
                    }
                    OrderStatusHistoryEntity statusHistory = OrderStatusHistoryEntity.builder()
                            .order(orderEntity)
                            .status(OrderStatus.CANCELLED)
                            .build();
                    orderEntity.getStatusHistory().add(statusHistory);
                        orderEntity.setStatus(OrderStatus.CANCELLED);
                        orderServiceRepo.save(orderEntity);
                        return orderEntity;
                    })
                .map(mapper::fromDBEntity)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
    }

    public OrderResponse deleteOrder(String id) {
        return orderServiceRepo.findById(id)//existsById() - a lightweight check, just used findById to return response
                .map(order-> {
                    orderServiceRepo.delete(order);
                    return order;
                })
                .map(deletedOrder->{
                    OrderResponse dOrder = mapper.fromDBEntity(deletedOrder);
                    dOrder.setStatus(OrderStatus.DELETED.toString());
                    return dOrder;
                })
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
    }

}
