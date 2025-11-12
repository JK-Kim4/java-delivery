package com.tutomato.delivery.domain.authentication;

import com.tutomato.delivery.domain.authentication.exception.AuthErrorCode;
import com.tutomato.delivery.domain.authentication.exception.UnAuthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final JwtTokenProperties properties;

    public JwtTokenProvider(JwtTokenProperties properties) {
        this.properties = properties;
    }

    @Override
    public Token generateToken(String identifier, Instant issuedAt, TokenType tokenType) {
        TokenProperty property = properties.getProperty(tokenType);
        Instant expiration = issuedAt.plus(property.ttl());

        return buildToken(identifier, tokenType, issuedAt, expiration, property.getKey());
    }

    @Override
    public Tokens generateTokenPair(String identifier, Instant now) {
        return new Tokens(
            buildToken(identifier, TokenType.ACCESS, now, now.plus(properties.access().ttl()), properties.access().getKey()),
            buildToken(identifier, TokenType.REFRESH, now, now.plus(properties.refresh().ttl()), properties.refresh().getKey())
        );
    }

    @Override
    public String getIdentifierFromAccessTokenValue(String value) {
        TokenProperty property = properties.getProperty(TokenType.ACCESS);

        return Jwts.parserBuilder()
            .setSigningKey(property.getKey())
            .build()
            .parseClaimsJws(value)
            .getBody().getSubject();
    }

    @Override
    public void validateTokenOrThrow(String value, TokenType tokenType) {
        TokenProperty property = properties.getProperty(tokenType);

        try {
            Jwts.parserBuilder()
                .setSigningKey(property.getKey())
                .build()
                .parseClaimsJws(value);
        } catch (ExpiredJwtException e) {
            logger.warn(AuthErrorCode.AUTH_EXPIRED_TOKEN.getMessage(), e);
            throw new UnAuthorizedException(AuthErrorCode.AUTH_EXPIRED_TOKEN);
        } catch (SignatureException e) {
            logger.warn(AuthErrorCode.AUTH_SIGNATURE_INVALID.getMessage(), e);
            throw new UnAuthorizedException(AuthErrorCode.AUTH_SIGNATURE_INVALID);
        } catch (MalformedJwtException e) {
            logger.warn(AuthErrorCode.AUTH_INVALID_TOKEN.getMessage(), e);
            throw new UnAuthorizedException(AuthErrorCode.AUTH_INVALID_TOKEN);
        } catch (Exception e) {
            logger.error("Token 검증 중 알 수 없는 오류 발생", e);
            throw e;
        }
    }

    private Token buildToken(
        String identifier,
        TokenType tokenType,
        Instant issuedAt,
        Instant expiration,
        Key secret
    ) {
        String token = Jwts.builder()
            .setSubject(identifier)
            .setIssuedAt(Date.from(issuedAt))
            .setExpiration(Date.from(expiration))
            .signWith(secret, SignatureAlgorithm.HS256)
            .compact();

        return new Token(token, tokenType, issuedAt, expiration);
    }

}
