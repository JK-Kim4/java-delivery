package com.tutomato.delivery.domain.authentication;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;

public record TokenProperty(
    String secret,
    Duration ttl
) {

    public Key getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
