package com.tutomato.delivery.domain.member;

import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.domain.BaseTimeEntity;
import com.tutomato.delivery.domain.member.exception.InvalidMemberException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.regex.Pattern;

@Entity
@Table(name = "members")
public class Member extends BaseTimeEntity {

    // account 생성 조건: 4~20자 영문 대소문자+숫자
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-z0-9A-Z]{4,20}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private Password password;

    private String name;

    private Role role;

    protected Member() {
    }

    private Member(
        String account,
        Password password,
        String name
    ) {
        validateAccount(account);
        validateName(name);

        this.account = account;
        this.password = password;
        this.name = name;
        this.role = Role.MEMBER;
    }

    public static Member create(
        String account,
        String rawPassword,
        String name,
        CryptoCipher cipher
    ) {
        Password password = Password.from(rawPassword, cipher);
        return new Member(account, password, name);
    }

    private static void validateAccount(String account) {
        if (account == null || account.isBlank()) {
            throw new InvalidMemberException("아이디(account)는 비어 있을 수 없습니다.");
        }

        if (!ACCOUNT_PATTERN.matcher(account).matches()) {
            throw new InvalidMemberException("아이디는 4~20자의 영문 소문자와 숫자만 사용할 수 있습니다.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidMemberException("이름(name)은 비어 있을 수 없습니다.");
        }

        if (name.length() > 20) {
            throw new InvalidMemberException("이름은 20자 이하여야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public Password getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
