package com.tutomato.delivery.domain.member;

import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.domain.BaseTimeEntity;
import com.tutomato.delivery.domain.member.exception.InvalidMemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Account account;

    @Embedded
    private Password password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(12)")
    private Role role;

    protected Member() {
    }

    private Member(
        Account account,
        Password password,
        String name,
        Role role
    ) {
        validateName(name);

        this.account = account;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static Member create(
        String account,
        String rawPassword,
        String name,
        Role role,
        CryptoCipher cipher
    ) {

        return new Member(
            Account.from(account),
            Password.from(rawPassword, cipher),
            name,
            role
        );
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidMemberException("이름(name)은 비어 있을 수 없습니다.");
        }

        if (name.length() > 20) {
            throw new InvalidMemberException("이름은 20자 이하여야 합니다.");
        }
    }

    public boolean isRiderMember() {
        return this.role == Role.RIDER;
    }

    public boolean isStoreMember() {
        return this.role == Role.STORE;
    }

    public boolean isCorrectPassword(String rawPassword, CryptoCipher cipher) {
        return this.password.matches(rawPassword, cipher);
    }

    public Long getId() {
        return id;
    }

    public String getAccount() {
        return account.getAccount();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId()) && Objects.equals(
            getAccount(), member.getAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAccount());
    }
}
