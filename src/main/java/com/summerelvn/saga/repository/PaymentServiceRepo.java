package com.summerelvn.saga.repository;

import com.summerelvn.saga.model.entities.OrderEntity;
import com.summerelvn.saga.model.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentServiceRepo extends JpaRepository<PaymentEntity, String> {
}
