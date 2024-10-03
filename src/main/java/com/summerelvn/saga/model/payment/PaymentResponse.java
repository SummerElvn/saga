package com.summerelvn.saga.model.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String id;
    private String name;
    private String type;
    private BigDecimal price;
    private String status;
    private List<PaymentResponse> orders;
}
