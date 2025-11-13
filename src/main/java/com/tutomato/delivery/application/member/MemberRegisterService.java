package com.tutomato.delivery.application.member;

import com.tutomato.delivery.application.member.dto.MemberRegisterCommand;
import com.tutomato.delivery.application.member.dto.MemberRegisterResult;
import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.Account;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.exception.MemberAlreadyExistException;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberRegisterService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRegisterService(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    public MemberRegisterResult register(MemberRegisterCommand command) {
        validateDuplicateAccount(command.account());

        Member member = Member.create(
            command.account(),
            command.rawPassword(),
            command.name(),
            command.role(),
            CryptoCipherFactory.get(CryptoAlgorithm.SHA256)
        );

        memberJpaRepository.save(member);

        return MemberRegisterResult.from(member);
    }

    private void validateDuplicateAccount(String account) {
        memberJpaRepository.findByAccount(Account.from(account))
            .ifPresent(existing -> {
                throw new MemberAlreadyExistException("이미 사용 중인 계정입니다. account=" + account);
            });
    }
}
