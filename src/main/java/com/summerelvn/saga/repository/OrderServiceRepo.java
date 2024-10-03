package com.summerelvn.saga.repository;

import com.summerelvn.saga.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderServiceRepo extends JpaRepository<OrderEntity, String> {
}
