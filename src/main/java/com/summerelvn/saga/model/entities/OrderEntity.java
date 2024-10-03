package com.summerelvn.saga.model.entities;

import com.summerelvn.saga.model.OrderStatus;
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
import java.util.UUID;

@AllArgsConstructor
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    /*
    @GeneratedValue(strategy = GenerationType.UUID)*/
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
    private OrderStatus status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    // Bidirectional mapping to OrderStatusHistory (Optional)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistoryEntity> statusHistory = new ArrayList<>();
}
