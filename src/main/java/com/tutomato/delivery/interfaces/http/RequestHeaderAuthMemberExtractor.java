package com.tutomato.delivery.interfaces.http;

import com.tutomato.delivery.common.exception.ForbiddenException;
import com.tutomato.delivery.common.extractor.AuthMemberExtractor;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.domain.authentication.JwtTokenProvider;
import com.tutomato.delivery.domain.authentication.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RequestHeaderAuthMemberExtractor implements AuthMemberExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;

    public RequestHeaderAuthMemberExtractor(HttpServletRequest request,
        JwtTokenProvider jwtTokenProvider) {
        this.request = request;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthMemberRequest extract() {
        String accessToken = extractAccessTokenFromHeader(request);

        validateAccessToken(accessToken);

        String identifier = jwtTokenProvider.getIdentifierFromAccessTokenValue(accessToken);

        if (!StringUtils.hasText(identifier)) {
            throw new ForbiddenException("유효한 사용자 식별 정보를 토큰에서 찾을 수 없습니다.");
        }

        return AuthMemberRequest.fromIdentifier(identifier);
    }

    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorization)) {
            throw new ForbiddenException("인증을 위한 Authorization 헤더가 존재하지 않습니다.");
        }

        if (!authorization.startsWith(BEARER_PREFIX)) {
            throw new ForbiddenException(
                "유효하지 않은 Authorization 헤더 형식입니다. 'Bearer {accessToken}' 형식이어야 합니다."
            );
        }

        String accessToken = authorization.substring(BEARER_PREFIX.length()).trim();

        if (!StringUtils.hasText(accessToken)) {
            throw new ForbiddenException("인증을 위한 access token 이 비어 있습니다.");
        }

        return accessToken;
    }

    private void validateAccessToken(String accessToken) {
        jwtTokenProvider.validateTokenOrThrow(accessToken, TokenType.ACCESS);
    }
}
