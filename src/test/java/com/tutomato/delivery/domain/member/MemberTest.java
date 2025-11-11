package com.tutomato.delivery.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.exception.InvalidMemberException;
import com.tutomato.delivery.domain.member.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    private final CryptoCipher cipher = CryptoCipherFactory.get(CryptoAlgorithm.SHA256);

    @Test
    @DisplayName("유효한 account, password, name 으로 Member.create 호출 시 Member가 정상 생성된다")
    void create_success() {
        // given
        String account = "TestUser1";
        String rawPassword = "Abcdef1234!@";
        String name = "홍길동";

        // when
        Member member = Member.create(account, rawPassword, name, cipher);

        // then
        assertThat(member.getId()).isNull();
        assertThat(member.getAccount()).isEqualTo(account);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getRole()).isEqualTo(Role.MEMBER);
        assertThat(member.getPassword()).isNotNull();
        assertThat(member.getPassword().getPassword()).isNotEqualTo(rawPassword);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("account가 null 또는 공백이면 InvalidMemberException을 던진다")
    void create_fail_invalid_account_null_or_blank(String blank) {
        // null
        assertThatThrownBy(() ->
            Member.create(blank, "Abcdef1234!@", "홍길동", cipher)
        )
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining("아이디(account)는 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "abc",
        "user!",
        "abcdefghijklmnopqrstu"
    })
    @DisplayName("account가 규칙(4~20자 영문 대소문자+숫자)에 맞지 않으면 InvalidMemberException을 던진다")
    void create_fail_invalid_account_pattern(String invalidAccount) {
        assertThatThrownBy(() ->
            Member.create(invalidAccount, "Abcdef1234!@", "홍길동", cipher)
        )
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining("아이디는 4~20자의 영문 대소문자와 숫자만 사용할 수 있습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("name이 null 또는 공백이면 InvalidMemberException을 던진다")
    void create_fail_invalid_name_null_or_blank(String blank) {
        // null
        assertThatThrownBy(() ->
            Member.create("TestUser1", "Abcdef1234!@", blank, cipher)
        )
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining("이름(name)은 비어 있을 수 없습니다.");
    }

    @Test
    @DisplayName("name이 20자를 초과하면 InvalidMemberException을 던진다")
    void create_fail_invalid_name_too_long() {
        String longName = "abcdefghijklmnopqrstu"; // 21자

        assertThatThrownBy(() ->
            Member.create("TestUser1", "Abcdef1234!@", longName, cipher)
        )
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining("이름은 20자 이하여야 합니다.");
    }

    @Test
    @DisplayName("password가 비밀번호 규칙을 만족하지 않으면 InvalidPasswordException을 던진다")
    void create_fail_invalid_password() {
        // 12자 미만
        String shortPassword = "Abc123!@"; // 8자

        assertThatThrownBy(() ->
            Member.create("TestUser1", shortPassword, "홍길동", cipher)
        )
            .isInstanceOf(InvalidPasswordException.class)
            .hasMessageContaining("비밀번호는 12자리 이상이어야 합니다.");
    }
  
}