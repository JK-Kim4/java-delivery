package com.tutomato.delivery.domain.authentication;

public record Tokens(
    Token accessToken,
    Token refreshToken
) {

}
