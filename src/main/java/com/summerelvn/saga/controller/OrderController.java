package com.summerelvn.saga.controller;

import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.model.order.OrderResponse;
import com.summerelvn.saga.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest){
            return ResponseEntity.ok(orderService.createOrder(orderRequest));

    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") String id){
            OrderResponse response =  orderService.getOrder(id);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
            return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/cancel-order/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("id") String id){
            OrderResponse response = orderService.cancelOrder(id);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable("id") String id){
        OrderResponse response = orderService.deleteOrder(id);
        return ResponseEntity.ok(response);
    }

}
