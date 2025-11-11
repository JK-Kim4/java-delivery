package com.tutomato.delivery.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    private final CryptoCipher cipher = CryptoCipherFactory.get(CryptoAlgorithm.SHA256);

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("비밀번호가 null, 빈 문자열, 공백일 경우 InvalidPasswordException 예외를 던진다")
    void password_fail_null_or_blank(String rawPassword) {
        assertThatThrownBy(() -> Password.from(rawPassword, cipher))
            .isInstanceOf(InvalidPasswordException.class)
            .hasMessageContaining("비밀번호가 비어있습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Ab1!",
        "Abcdef1!",
        "Abcdefg123!",}
    )
    @DisplayName("비밀번호가 12자리 미만일 경우 InvalidPasswordException 예외를 던진다")
    void password_fail_too_short(String rawPassword) {
        assertThatThrownBy(() -> Password.from(rawPassword, cipher))
            .isInstanceOf(InvalidPasswordException.class)
            .hasMessageContaining("비밀번호는 12자리 이상이어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Abcdef1234!@",    // 대문자 + 소문자 + 숫자 + 특수문자 (12자리)
        "ABCDEFGhijk1",    // 대문자 + 소문자 + 숫자
        "abcdEFGH!@#1",    // 대문자 + 소문자 + 특수문자
        "aaaaBBBB1111",    // 대문자 + 소문자 + 숫자
    })
    @DisplayName("비밀번호 규칙을 만족하는 경우 Password 객체 생성에 성공한다")
    void password_success(String rawPassword) {
        assertThat(Password.from(rawPassword, cipher)).isNotNull();
    }
}