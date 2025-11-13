package com.tutomato.delivery.application.member;

import com.tutomato.delivery.application.member.dto.MemberAuthenticateCommand;
import com.tutomato.delivery.application.member.dto.MemberAuthenticateResult;
import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.authentication.JwtTokenProvider;
import com.tutomato.delivery.domain.authentication.Tokens;
import com.tutomato.delivery.domain.member.Account;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.exception.InvalidPasswordException;
import com.tutomato.delivery.domain.member.exception.MemberNotFountException;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberAuthenticateService {

    private final MemberJpaRepository memberJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberAuthenticateService(
        MemberJpaRepository memberJpaRepository,
        JwtTokenProvider tokenProvider
    ) {
        this.memberJpaRepository = memberJpaRepository;
        this.jwtTokenProvider = tokenProvider;
    }

    public MemberAuthenticateResult authentication(
        MemberAuthenticateCommand command
    ) {
        // 0. 인증 요청 시각
        Instant now = Instant.now();

        // 1. 사용자 조회
        Member member = memberJpaRepository.findByAccount(Account.from(command.account()))
            .orElseThrow(() -> new MemberNotFountException("입력하신 아이디에 해당하는 사용자를 찾을 수 없습니다."));

        // 2. password 일치 여부 검증
        validationPassword(member, command.rawPassword());

        // 3. identifier 생성
        String identifier = generateMemberIdentifier(member);

        // 4.토큰 생성
        Tokens tokens = jwtTokenProvider.generateTokenPair(identifier, now);

        // 5. 반환
        return MemberAuthenticateResult.of(member, tokens);
    }

    private void validationPassword(Member member, String rawPassword) {
        if (!member.isCorrectPassword(rawPassword, CryptoCipherFactory.get(CryptoAlgorithm.SHA256))) {
            throw new InvalidPasswordException("사용자의 비밀번호가 일치하지않습니다.");
        }
    }

    // TODO: Token에 포함되어있는 사용자 인증값 정의 필요
    // 현재 access token 내 사용자 인증 정보는 {memberId},{memberAccount} 정보를 문자열 형태로 전달
    private String generateMemberIdentifier(Member member) {
        return member.getId() + "," + member.getAccount();
    }
}
