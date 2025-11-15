package com.tutomato.delivery.interfaces.delivery.dto;

import java.time.Duration;

public enum DeliverySearchPeriod {

    M30(Duration.ofMinutes(30)),
    H1(Duration.ofHours(1)),
    H3(Duration.ofHours(3)),
    H6(Duration.ofHours(6)),
    H9(Duration.ofHours(9)),
    H12(Duration.ofHours(12)),
    D1(Duration.ofDays(1)),
    D2(Duration.ofDays(2)),
    D3(Duration.ofDays(3));

    private final Duration duration;

    DeliverySearchPeriod(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
