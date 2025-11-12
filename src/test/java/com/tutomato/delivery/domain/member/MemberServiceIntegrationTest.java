package com.tutomato.delivery.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tutomato.delivery.application.member.MemberRegisterService;
import com.tutomato.delivery.application.member.dto.MemberRegisterCommand;
import com.tutomato.delivery.application.member.dto.MemberRegisterResult;
import com.tutomato.delivery.domain.member.exception.MemberAlreadyExistException;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class MemberServiceIntegrationTest {

    @Autowired
    private MemberRegisterService memberRegisterService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("신규 account로 회원 가입 시 회원이 저장되고 결과를 반환한다")
    void registerMember_success() {
        // given
        MemberRegisterCommand command = new MemberRegisterCommand(
            "testaccount",
            "Abcdef1234!@",
            "홍길동"
        );

        // when
        MemberRegisterResult result = memberRegisterService.register(command);

        // then
        assertThat(result.memberId()).isNotNull();
        assertThat(result.account()).isEqualTo(command.account());
        assertThat(result.name()).isEqualTo(command.name());
        assertThat(result.role()).isEqualTo(Role.MEMBER);
        assertThat(result.createdAt()).isNotNull();

        // 실제 DB에 저장된 엔티티 검증
        Member saved = memberJpaRepository.findById(result.memberId())
            .orElseThrow();

        assertThat(saved.getAccount()).isEqualTo(command.account());
        assertThat(saved.getName()).isEqualTo(command.name());
        assertThat(saved.getPassword()).isNotNull();
        assertThat(saved.getPassword().getPassword()).isNotEqualTo(command.rawPassword());
    }

    @Test
    @DisplayName("이미 존재하는 account로 회원 가입 시 MemberAlreadyExistException 예외를 던진다")
    void registerMember_duplicateAccount() {
        // given
        String account = "duplicationaccount";

        MemberRegisterCommand first = new MemberRegisterCommand(
            account,
            "Abcdef1234!@",
            "첫번째회원"
        );
        memberRegisterService.register(first);

        MemberRegisterCommand second = new MemberRegisterCommand(
            account,
            "Abcdef1234!@",
            "두번째회원"
        );

        // when & then
        assertThatThrownBy(() -> memberRegisterService.register(second))
            .isInstanceOf(MemberAlreadyExistException.class)
            .hasMessageContaining("이미 사용 중인 계정입니다");
    }

    @BeforeEach
    void setUp() {
        memberJpaRepository.deleteAll();
    }

}