package com.tutomato.delivery.domain.member;

import com.tutomato.delivery.domain.member.exception.InvalidMemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Account {

    // 4~20자 영문 대소문자 + 숫자
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z0-9]{4,20}$");

    @Column(name = "account", nullable = false, length = 20, unique = true)
    private String account;

    protected Account() {
    }

    private Account(String account) {
        validate(account);
        this.account = account;
    }

    public static Account from(String account) {
        return new Account(account);
    }

    private static void validate(String account) {
        if (account == null || account.isBlank()) {
            throw new InvalidMemberException("아이디(account)는 비어 있을 수 없습니다.");
        }

        if (!ACCOUNT_PATTERN.matcher(account).matches()) {
            throw new InvalidMemberException("아이디는 4~20자의 영문 대소문자와 숫자만 사용할 수 있습니다.");
        }
    }

    public String getAccount() {
        return account;
    }

    // 값 객체이므로 equals / hashCode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(account, account.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account);
    }

}
