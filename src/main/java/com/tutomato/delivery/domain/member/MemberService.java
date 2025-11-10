package com.tutomato.delivery.domain.member;

import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.dto.RegisterMemberCommand;
import com.tutomato.delivery.domain.member.dto.RegisterMemberResult;
import com.tutomato.delivery.domain.member.exception.MemberAlreadyExistException;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public RegisterMemberResult register(RegisterMemberCommand command) {
        validateDuplicateAccount(command.account());

        Member member = Member.create(
            command.account(),
            command.rawPassword(),
            command.name(),
            CryptoCipherFactory.get(CryptoAlgorithm.SHA256)
        );

        memberJpaRepository.save(member);

        return RegisterMemberResult.from(member);
    }

    private void validateDuplicateAccount(String account) {
        memberJpaRepository.findByAccount(account)
            .ifPresent(existing -> {
                throw new MemberAlreadyExistException("이미 사용 중인 계정입니다. account=" + account);
            });
    }
}
