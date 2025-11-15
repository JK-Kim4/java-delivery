package com.tutomato.delivery.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import org.springframework.http.HttpStatus;

@Schema(description = "공통 에러 응답")
public record ErrorResponse(

    @Schema(description = "HTTP 상태 코드", example = "403")
    int status,

    @Schema(description = "에러 요약 코드 또는 이름", example = "FORBIDDEN")
    String error,

    @Schema(description = "에러 상세 메시지", example = "접근 권한이 없습니다.")
    String message,

    @Schema(description = "요청 경로", example = "/api/deliveries/1")
    String path,

    @Schema(description = "에러 발생 시각(UTC)", example = "2025-11-15T12:34:56Z")
    Instant timestamp
) {

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return new ErrorResponse(
            status.value(),
            status.name(),
            message,
            path,
            Instant.now()
        );
    }
}
