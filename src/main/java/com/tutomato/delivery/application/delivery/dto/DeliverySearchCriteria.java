package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.interfaces.delivery.dto.DeliverySearchPeriod;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record DeliverySearchCriteria(
    Long riderId,
    Instant from,
    Instant to,
    List<DeliveryStatus> statuses
) {

    private static final Duration MAX_SEARCH_DURATION = Duration.ofDays(3);

    public static DeliverySearchCriteria of(
        Long riderId,
        DeliverySearchPeriod period,
        List<DeliveryStatus> statuses
    ) {
        Instant now = Instant.now();
        Instant from = now.minus(period.getDuration());

        validateDuration(from, now);

        List<DeliveryStatus> normalizedStatuses =
            (statuses == null || statuses.isEmpty())
                ? List.of(DeliveryStatus.values())
                : List.copyOf(statuses);


        return new DeliverySearchCriteria(riderId, from, now, normalizedStatuses);

    }

    private static void validateDuration(Instant from, Instant to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("조회 시작 시간은 종료 시간보다 이후일 수 없습니다.");
        }

        Duration between = Duration.between(from, to);
        if (between.compareTo(MAX_SEARCH_DURATION) > 0) {
            throw new IllegalArgumentException("조회 가능한 최대 기간은 3일을 초과할 수 없습니다.");
        }
    }

}
