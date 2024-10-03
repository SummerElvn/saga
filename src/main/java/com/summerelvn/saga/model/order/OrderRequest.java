package com.summerelvn.saga.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    String id; // for orchestrator need
    String name;
    String type;
    BigDecimal price;
}
