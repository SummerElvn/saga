package com.summerelvn.saga.model.entities;

import com.summerelvn.saga.model.OrderStatus;
import com.summerelvn.saga.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private  String type;
    @Column
    private  BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Column
    private PaymentStatus status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
