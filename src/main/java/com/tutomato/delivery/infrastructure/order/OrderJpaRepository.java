package com.tutomato.delivery.infrastructure.order;

import com.tutomato.delivery.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}
