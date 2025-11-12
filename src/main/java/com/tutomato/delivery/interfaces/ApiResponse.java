package com.tutomato.delivery.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "표준 API 응답 wrapper 클래스")
public record ApiResponse<T>(

    @Schema(description = "응답 상태", example = "success")
    String status,

    @Schema(description = "메시지", example = "로그인 성공")
    String message,

    @Schema(description = "결과", example = "{data object}")
    T data
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    @Override
    public String status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public T data() {
        return data;
    }
}
