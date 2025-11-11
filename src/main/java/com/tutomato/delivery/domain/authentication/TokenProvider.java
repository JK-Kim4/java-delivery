package com.tutomato.delivery.domain.authentication;

import java.time.Instant;

public interface TokenProvider {

    Token generateToken(String identifier, Instant now, TokenType tokenType);

    Tokens generateTokenPair(String identifier, Instant now);

    String getIdentifierFromAccessTokenValue(String value);

    void validateTokenOrThrow(String value, TokenType tokenType);

}
