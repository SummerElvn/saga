package com.summerelvn.saga.model.entities;

import com.summerelvn.saga.model.PaymentStatus;
import com.summerelvn.saga.model.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "shipments")
public class ShipmentEntity {
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
    private ShipmentStatus status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
