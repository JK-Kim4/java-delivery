package com.tutomato.delivery.domain.member;

import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.domain.member.exception.InvalidPasswordException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    protected Password() {
    }

    private Password(String password) {
        this.password = password;
    }

    public static Password from(String rawPassword, CryptoCipher cipher) {
        validate(rawPassword);
        String encrypted = cipher.encrypt(rawPassword);
        return new Password(encrypted);
    }

    public static void validate(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidPasswordException("비밀번호가 비어있습니다.");
        }

        if (password.length() < 12) {
            throw new InvalidPasswordException("비밀번호는 12자리 이상이어야 합니다.");
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else {
                // 영문/숫자가 아닌 모든 문자 → 특수문자로 취급
                hasSpecial = true;
            }
        }

        int typeCount = 0;
        if (hasUpper) {
            typeCount++;
        }
        if (hasLower) {
            typeCount++;
        }
        if (hasDigit) {
            typeCount++;
        }
        if (hasSpecial) {
            typeCount++;
        }

        if (typeCount < 3) {
            throw new InvalidPasswordException(
                "비밀번호는 영어 대문자, 영어 소문자, 숫자, 특수문자 중 3종류 이상을 포함해야 합니다."
            );
        }
    }

    public boolean matches(String rawPassword, CryptoCipher cipher) {
        return cipher.matches(rawPassword, this.password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }
}
