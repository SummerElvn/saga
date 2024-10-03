package com.summerelvn.saga.controller;

import com.summerelvn.saga.model.payment.PaymentRequest;
import com.summerelvn.saga.model.payment.PaymentResponse;
import com.summerelvn.saga.model.shipping.ShipmentRequest;
import com.summerelvn.saga.model.shipping.ShipmentResponse;
import com.summerelvn.saga.service.PaymentService;
import com.summerelvn.saga.service.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShippingController {

    private final ShippingService shippingService;
    public ShippingController(ShippingService shippingService){
            this.shippingService = shippingService;
    }

    @PostMapping("/process-shipment")
    public ResponseEntity<ShipmentResponse> processShipment(@RequestBody ShipmentRequest shipmentRequest){
        return ResponseEntity.ok(shippingService.processShipment(shipmentRequest));

    }

    @GetMapping("/get-shipment/{id}")
    public ResponseEntity<ShipmentResponse> getShipment(@PathVariable("id") String id){
        ShipmentResponse response =  shippingService.getShipment(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-shipments")
    public ResponseEntity<List<ShipmentResponse>> getAllShipment(){
        return ResponseEntity.ok(shippingService.getAllShipments());
    }

    @PutMapping("/cancel-shipment/{id}")
    public ResponseEntity<ShipmentResponse> cancelShipment(@PathVariable("id") String id){
        ShipmentResponse response = shippingService.cancelShipment(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-shipment/{id}")
    public ResponseEntity<ShipmentResponse> deleteShipment(@PathVariable("id") String id){
        ShipmentResponse response = shippingService.deleteShipment(id);
        return ResponseEntity.ok(response);
    }
}
