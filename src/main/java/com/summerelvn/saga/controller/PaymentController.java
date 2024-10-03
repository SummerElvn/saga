package com.summerelvn.saga.controller;

import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService){
            this.paymentService = paymentService;
    }

    @PostMapping("/process-payment")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.processPayment(paymentRequest));

    }

    @GetMapping("/get-payment/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable("id") String id){
        PaymentResponse response =  paymentService.getPayment(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PutMapping("/cancel-payment/{id}")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable("id") String id){
        PaymentResponse response = paymentService.cancelPayment(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-payment/{id}")
    public ResponseEntity<PaymentResponse> deletePayment(@PathVariable("id") String id){
        PaymentResponse response = paymentService.deletePayment(id);
        return ResponseEntity.ok(response);
    }
}
