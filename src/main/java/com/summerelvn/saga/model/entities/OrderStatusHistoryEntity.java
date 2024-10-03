package com.summerelvn.saga.model.entities;

import com.summerelvn.saga.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name = "order_status_history")
public class OrderStatusHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status;

    @CreationTimestamp
    @Column
    private LocalDateTime statusUpdatedAt;
}
