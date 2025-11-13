package com.tutomato.delivery.infrastructure.delivery;

import com.tutomato.delivery.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {

}
