package com.tutomato.delivery.application.delivery;

import com.tutomato.delivery.application.delivery.dto.DeliverySearchCriteria;
import com.tutomato.delivery.application.delivery.dto.DeliverySearchResult;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.infrastructure.delivery.DeliveryJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DeliveryQueryService {

    private final DeliveryJpaRepository deliveryJpaRepository;

    public DeliveryQueryService(DeliveryJpaRepository deliveryJpaRepository) {
        this.deliveryJpaRepository = deliveryJpaRepository;
    }

    public List<DeliverySearchResult> search(DeliverySearchCriteria criteria) {

        List<Delivery> deliveries = deliveryJpaRepository.searchDeliveries(
            criteria.riderId(), criteria.from(), criteria.to(), criteria.statuses()
        );

        return DeliverySearchResult.fromList(deliveries);
    }

}
