package com.tutomato.delivery.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeParser {

    private static final ZoneId LOCAL_ZOME = ZoneId.of("Asia/Seoul");

    public static LocalDateTime toLocalDateTimeOrNull(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, LOCAL_ZOME);
    }
}
