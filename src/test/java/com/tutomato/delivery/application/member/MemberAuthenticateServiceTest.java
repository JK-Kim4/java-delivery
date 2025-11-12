package com.tutomato.delivery.application.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.tutomato.delivery.application.member.dto.MemberAuthenticateCommand;
import com.tutomato.delivery.application.member.dto.MemberAuthenticateResult;
import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberAuthenticateServiceTest {

    @Autowired
    private MemberAuthenticateService memberAuthenticateService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("가입된 회원이 올바른 비밀번호로 인증을 요청하면 토큰을 발급한다")
    void authentication_success() {
        // given
        String account = "testaccount";
        String rawPassword = "Abcdef1234!@";
        String name = "홍길동";

        CryptoCipher cipher = CryptoCipherFactory.get(CryptoAlgorithm.SHA256);

        Member member = Member.create(
            account,
            rawPassword,
            name,
            cipher
        );
        memberJpaRepository.save(member);

        MemberAuthenticateCommand command = new MemberAuthenticateCommand(
            account,
            rawPassword
        );

        // when
        MemberAuthenticateResult result = memberAuthenticateService.authentication(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.tokens().accessToken()).isNotNull();
        assertThat(result.tokens().refreshToken()).isNotNull();
    }

}