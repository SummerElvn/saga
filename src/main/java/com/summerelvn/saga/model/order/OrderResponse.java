package com.summerelvn.saga.model.order;

import com.summerelvn.saga.exception.ErrorResponse;
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
public class OrderResponse {
    private String id;
    private String name;
    private String type;
    private BigDecimal price;
    private String status;
    private List<OrderResponse> orders;
}
