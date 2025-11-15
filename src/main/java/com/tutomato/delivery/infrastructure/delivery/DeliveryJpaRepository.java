package com.tutomato.delivery.infrastructure.delivery;

import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {


    @Query("""
        select d
        from Delivery d
        where 1 = 1
          and d.createdAt between :from and :to
          and (:riderId is null or d.rider.id = :riderId)
          and d.deliveryStatus in :statuses
        order by d.createdAt desc
        """)
    List<Delivery> searchDeliveries(
        @Param("riderId") Long riderId,
        @Param("from") Instant from,
        @Param("to") Instant to,
        @Param("statuses") List<DeliveryStatus> statuses
    );

}
