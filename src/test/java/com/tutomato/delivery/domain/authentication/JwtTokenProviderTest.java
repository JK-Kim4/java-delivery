package com.tutomato.delivery.domain.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tutomato.delivery.domain.authentication.exception.UnAuthorizedException;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private static final String BASE64_SECRET = "CnzOb1c6g3kkY8QW+TwFl1DgNBFWtSEZamaHS+0BA/U=";

    @BeforeEach
    void setUp() {
        // access / refresh 각각 설정
        TokenProperty access = new TokenProperty(
            BASE64_SECRET,
            Duration.ofMinutes(15) // access TTL 15분
        );

        TokenProperty refresh = new TokenProperty(
            BASE64_SECRET,
            Duration.ofDays(7) // refresh TTL 7일
        );

        JwtTokenProperties properties = new JwtTokenProperties(access, refresh);
        jwtTokenProvider = new JwtTokenProvider(properties);
    }

    @Test
    @DisplayName("Access Token 생성 시 토큰이 정상 발급되고 검증 및 식별자 조회가 성공한다")
    void generateAccessToken_andValidate_success() {
        // given
        String identifier = "member-1";
        Instant issuedAt = Instant.now();

        // when
        Token token = jwtTokenProvider.generateToken(identifier, issuedAt, TokenType.ACCESS);

        // then
        assertThat(token).isNotNull();
        assertThat(token.value()).isNotBlank();
        assertThat(token.tokenType()).isEqualTo(TokenType.ACCESS);
        assertThat(token.expiration())
            .isAfter(issuedAt)
            .isBefore(issuedAt.plus(Duration.ofMinutes(16)));

        // 토큰 검증
        jwtTokenProvider.validateTokenOrThrow(token.value(), TokenType.ACCESS);

        // subject(identifier) 추출 검증
        String parsedIdentifier = jwtTokenProvider.getIdentifierFromAccessTokenValue(token.value());
        assertThat(parsedIdentifier).isEqualTo(identifier);
    }

    @Test
    @DisplayName("만료된 Access Token 검증 시 만료 예외가 발생한다")
    void validateToken_expiredToken_throwsUnAuthorizedException() {
        TokenProperty expiredAccess = new TokenProperty(
            BASE64_SECRET,
            Duration.ofSeconds(-10)
        );
        TokenProperty dummyRefresh = new TokenProperty(
            BASE64_SECRET,
            Duration.ofDays(7)
        );
        JwtTokenProperties expiredProps = new JwtTokenProperties(expiredAccess, dummyRefresh);
        JwtTokenProvider expiredProvider = new JwtTokenProvider(expiredProps);

        Instant issuedAt = Instant.now();
        Token expiredToken = expiredProvider.generateToken("member-1", issuedAt, TokenType.ACCESS);

        // when & then
        assertThatThrownBy(() ->
            expiredProvider.validateTokenOrThrow(expiredToken.value(), TokenType.ACCESS)
        ).isInstanceOf(UnAuthorizedException.class);
    }

}