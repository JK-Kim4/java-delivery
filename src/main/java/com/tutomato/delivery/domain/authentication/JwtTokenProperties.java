package com.tutomato.delivery.domain.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtTokenProperties(
    TokenProperty access,
    TokenProperty refresh
) {

    public TokenProperty getProperty(TokenType tokenType) {
        if (tokenType == TokenType.ACCESS) {
            return access;
        } else {
            return refresh;
        }
    }
}

