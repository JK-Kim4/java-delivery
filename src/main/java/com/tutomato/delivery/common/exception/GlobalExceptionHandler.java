package com.tutomato.delivery.common.exception;

import com.tutomato.delivery.domain.authentication.exception.AuthErrorCode;
import com.tutomato.delivery.domain.authentication.exception.UnAuthorizedException;
import com.tutomato.delivery.domain.delivery.exception.DeliveryNotFoundException;
import com.tutomato.delivery.domain.delivery.exception.IllegalDeliveryStatusException;
import com.tutomato.delivery.domain.delivery.exception.IllegalRiderException;
import com.tutomato.delivery.domain.member.exception.IllegalMemberRoleException;
import com.tutomato.delivery.domain.member.exception.InvalidMemberException;
import com.tutomato.delivery.domain.member.exception.InvalidPasswordException;
import com.tutomato.delivery.domain.member.exception.MemberAlreadyExistException;
import com.tutomato.delivery.domain.member.exception.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 글로벌 예외 핸들러.
 * <p>
 * - 각 도메인/레이어에서 발생한 커스텀 예외를 잡아서 공통 에러 응답 형식으로 변환한다.
 * - 여기서 반환하는 ResponseEntity<ErrorResponse> 가 클라이언트에 내려가는 에러 본문이 된다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * ForbiddenException을 **HTTP 4xx (주로 403 Forbidden)** 으로 변환하는 전역 예외 처리기.
     * 인증은 되었지만, 해당 리소스나 기능에 대한 **접근 권한이 없는 경우**에 사용됩니다.
     *
     * 동작:
     *  - ForbiddenException 내부의 HttpStatus를 읽어와 응답 코드로 사용합니다.
     *  - 요청 HTTP 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 에러 응답을 생성합니다.
     *
     * 사용 예:
     *  - 다른 회원의 리소스에 접근하려 할 때 권한이 없는 경우
     *  - 해당 역할(Role)로 호출할 수 없는 API를 요청한 경우
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
        ForbiddenException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getHttpStatus();

        logger.warn("[Forbidden] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * MethodArgumentNotValidException을 **HTTP 400 (Bad Request)** 로 변환하는 전역 예외 처리기.
     * **@RequestBody DTO 바인딩/검증(@Valid) 실패** 시 필드별 오류를 수집해 응답합니다.
     *
     * 동작:
     *  - `BindingResult.fieldErrors`를 모아 `"field: message"` 형태로 요약합니다.
     *  - 요청 URI와 요약 메시지를 **에러 로그**로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 **HTTP 400** 응답을 생성합니다.
     *
     * 사용 예:
     *  - 필수 필드 누락, 형식/길이 제약 위반, enum 허용값 위반 등
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> fieldMessages = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .distinct()
            .collect(Collectors.toList());

        String joinedMessage = String.join(", ", fieldMessages);
        String message = fieldMessages.isEmpty()
            ? "요청 값이 유효하지 않습니다."
            : "요청 값이 유효하지 않습니다. (" + joinedMessage + ")";

        logger.warn("[MethodArgumentNotValid] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            message
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            message,
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * IllegalDeliveryStatusException을 **HTTP 4xx (주로 400 / 409)** 로 변환하는 전역 예외 처리기.
     * 배달 엔티티의 **허용되지 않은 상태 전이** 또는 **현재 상태에서 불가능한 작업**을 시도할 때 사용됩니다.
     *
     * 동작:
     *  - 예외 객체에서 HttpStatus를 읽어와 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 응답을 생성합니다.
     *
     * 사용 예:
     *  - 이미 완료된 배달을 다시 완료/취소 처리하려는 경우
     *  - 배달 대기 상태가 아닌데 배달 시작/완료 API를 호출한 경우
     */
    @ExceptionHandler(IllegalDeliveryStatusException.class)
    public ResponseEntity<ErrorResponse> handleIllegalDeliveryStatus(
        IllegalDeliveryStatusException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getHttpStatus();

        logger.warn("[IllegalDeliveryStatus] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * IllegalMemberRoleException을 **HTTP 4xx (주로 403 Forbidden)** 으로 변환하는 전역 예외 처리기.
     * 회원의 **역할/권한(Role)이 요구사항을 만족하지 않을 때** 사용됩니다.
     *
     * 동작:
     *  - 예외 객체에 정의된 HttpStatus를 읽어와 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 응답을 생성합니다.
     *
     * 사용 예:
     *  - RIDER만 호출 가능한 API를 MEMBER가 호출하는 경우
     *  - ADMIN 권한이 필요한 관리용 API를 일반 회원이 호출하는 경우
     */
    @ExceptionHandler(IllegalMemberRoleException.class)
    public ResponseEntity<ErrorResponse> handleIllegalMemberRole(
        IllegalMemberRoleException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[IllegalMemberRole] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * DeliveryNotFoundException을 **HTTP 404 (Not Found)** 로 변환하는 전역 예외 처리기.
     * 요청한 배달 ID에 해당하는 배달 정보를 찾을 수 없을 때 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus(주로 404)를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 404 응답을 생성합니다.
     *
     * 사용 예:
     *  - 존재하지 않는 배달 번호로 조회/수정을 시도한 경우
     *  - 이미 삭제되었거나 취소된 배달을 다시 조회하는 경우
     */
    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryNotFound(
        DeliveryNotFoundException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[DeliveryNotFound] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * IllegalRiderException을 **HTTP 4xx** 로 변환하는 전역 예외 처리기.
     * 배달과 연관된 **라이더 검증 실패** (예: 다른 라이더의 배달 접근) 상황에서 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 응답을 생성합니다.
     *
     * 사용 예:
     *  - 현재 로그인한 라이더와 배달에 할당된 라이더가 다른 경우
     *  - 라이더가 아닌 사용자가 라이더 전용 배달 API를 호출한 경우
     */
    @ExceptionHandler(IllegalRiderException.class)
    public ResponseEntity<ErrorResponse> handleIllegalRider(
        IllegalRiderException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[IllegalRider] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * InvalidMemberException을 **HTTP 4xx** 로 변환하는 전역 예외 처리기.
     * 회원 도메인 생성/수정 시 **유효하지 않은 값(아이디/이름 등)** 으로 인해 검증이 실패했을 때 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 응답을 생성합니다.
     *
     * 사용 예:
     *  - 아이디 패턴, 이름 길이, 기타 도메인 규칙 위반으로 Member 생성이 거부된 경우
     *  - 도메인 내부 유효성 검사에서 실패한 경우
     */
    @ExceptionHandler(InvalidMemberException.class)
    public ResponseEntity<ErrorResponse> handleInValidMemberRole(
        InvalidMemberException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[InvalidMember] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * InvalidPasswordException을 **HTTP 4xx** 로 변환하는 전역 예외 처리기.
     * 로그인/인증 또는 비밀번호 변경 시 **비밀번호가 도메인 규칙에 맞지 않거나 일치하지 않을 때** 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx 응답을 생성합니다.
     *
     * 사용 예:
     *  - 로그인 시 입력한 비밀번호가 저장된 비밀번호와 일치하지 않는 경우
     *  - 비밀번호 변경 시 기존 비밀번호 검증이 실패한 경우
     *  - 비밀번호 복잡도/패턴(대소문자, 숫자, 특수문자 등) 기준을 만족하지 못한 경우
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInValidPassword(
        InvalidPasswordException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[InvalidPassword] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * MemberAlreadyExistException을 **HTTP 409 (Conflict)** 등으로 변환하는 전역 예외 처리기.
     * 회원 가입 시 **이미 존재하는 계정/회원** 으로 인해 충돌이 발생한 경우 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus(주로 409)를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 4xx/409 응답을 생성합니다.
     *
     * 사용 예:
     *  - 동일한 account로 이미 가입된 회원이 존재하는 경우
     *  - 중복된 고유 식별자를 가진 회원을 생성하려는 경우
     */
    @ExceptionHandler(MemberAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleMemberAlreadyExist(
        MemberAlreadyExistException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[MemberAlreadyExist] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * MemberNotFoundException을 **HTTP 404 (Not Found)** 로 변환하는 전역 예외 처리기.
     * 요청한 조건(account, id 등)에 해당하는 회원을 찾을 수 없을 때 사용됩니다.
     *
     * 동작:
     *  - 예외에 포함된 HttpStatus(주로 404)를 응답 코드로 사용합니다.
     *  - 요청 메서드, URI, 예외 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 404 응답을 생성합니다.
     *
     * 사용 예:
     *  - 로그인 시 입력한 account에 해당하는 회원이 존재하지 않는 경우
     *  - ID 기반으로 회원 조회/수정을 시도했지만 이미 탈퇴/삭제된 경우
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFound(
        MemberNotFoundException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        logger.warn("[MemberNotFount] {} {} - {}",
            request.getMethod(),
            request.getRequestURI(),
            ex.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * UnAuthorizedException을 **인증/인가 오류 코드(AuthErrorCode)** 기반의 HTTP 상태 코드로 변환하는 전역 예외 처리기.
     * 주로 JWT 토큰 기반 인증에서 발생하는 **인증 실패/인가 거부** 상황을 처리합니다.
     *
     * 동작:
     *  - 예외에 포함된 `AuthErrorCode`에서 HttpStatus와 사용자용 메시지를 가져옵니다.
     *  - 요청 메서드, URI, 에러 코드명, 상세 메시지를 **경고 로그(warn)** 로 남깁니다.
     *  - 표준 `ErrorResponse` 포맷으로 401/403 등의 HTTP 응답을 생성합니다.
     *
     * 사용 예:
     *  - Access Token 누락, 만료, 위조 등으로 인증에 실패한 경우
     *  - 지원하지 않는 토큰 타입/스킴으로 인증을 시도한 경우
     *  - Refresh Token 검증 실패 등 인증 관련 공통 에러 처리
     */
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorized(
        UnAuthorizedException ex,
        HttpServletRequest request
    ) {
        AuthErrorCode code = ex.getAuthErrorCode();
        HttpStatus status = code.getHttpStatus();

        logger.warn("[AuthError] {} {} - {} ({})",
            request.getMethod(),
            request.getRequestURI(),
            code.name(),
            code.getMessage()
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            code.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * 위에서 별도로 처리하지 않은 모든 예외를 **HTTP 500 (Internal Server Error)** 로 변환하는
     * 글로벌 Fallback 예외 처리기.
     * <p>
     * 시스템 내부 구현 상세를 노출하지 않고, **일반화된 서버 오류 메시지**를 클라이언트에 반환합니다.
     *
     * 동작:
     *  - 예외 스택 트레이스를 포함하여 **에러 로그(error)** 로 남깁니다.
     *  - 클라이언트에는 "서버 내부 오류가 발생했습니다." 와 같은 일반 메시지를 전달합니다.
     *  - 표준 `ErrorResponse` 포맷으로 HTTP 500 응답을 생성합니다.
     *
     * 사용 예:
     *  - 위의 @ExceptionHandler 메서드들로 처리되지 않은 모든 런타임 예외
     *  - 예기치 못한 NPE, 외부 라이브러리 예외, 인프라 관련 예외 등
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
        Exception ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        logger.error("[UnhandledException] {} {}",
            request.getMethod(),
            request.getRequestURI(),
            ex
        );

        ErrorResponse body = ErrorResponse.of(
            status,
            "서버 내부 오류가 발생했습니다.",
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }
}