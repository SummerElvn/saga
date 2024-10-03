package com.summerelvn.saga.repository;

import com.summerelvn.saga.model.entities.PaymentEntity;
import com.summerelvn.saga.model.entities.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingServiceRepo extends JpaRepository<ShipmentEntity, String> {
}
