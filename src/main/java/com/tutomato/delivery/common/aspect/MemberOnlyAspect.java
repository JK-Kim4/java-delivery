package com.tutomato.delivery.common.aspect;

import com.tutomato.delivery.common.exception.ForbiddenException;
import com.tutomato.delivery.common.extractor.AuthMemberExtractor;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberOnlyAspect {

    private final AuthMemberExtractor authMemberExtractor;
    private final MemberJpaRepository memberJpaRepository;

    public MemberOnlyAspect(AuthMemberExtractor authMemberExtractor,
        MemberJpaRepository memberJpaRepository
    ) {
        this.authMemberExtractor = authMemberExtractor;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Around(
        "@within(com.tutomato.delivery.common.annotation.RiderOnly) " +
            "|| @annotation(com.tutomato.delivery.common.annotation.RiderOnly)"
    )
    public Object validateRiderOnlyRequest(ProceedingJoinPoint pjp) throws Throwable {
        Member rider = extractMemberOrThrow();

        if (!rider.isRiderMember()) {
            throw new ForbiddenException("권한이 존재하지 않는 사용자입니다.");
        }

        return pjp.proceed();
    }

    @Around(
        "@within(com.tutomato.delivery.common.annotation.StoreOnly) " +
            "|| @annotation(com.tutomato.delivery.common.annotation.StoreOnly)"
    )
    public Object validateStoreOnlyRequest(ProceedingJoinPoint pjp) throws Throwable {
        Member store = extractMemberOrThrow();

        if (!store.isStoreMember()) {
            throw new ForbiddenException("권한이 존재하지 않는 사용자입니다.");
        }

        return pjp.proceed();
    }

    private Member extractMemberOrThrow() {
        AuthMemberRequest authMemberRequest = authMemberExtractor.extract();

        return memberJpaRepository.findById(authMemberRequest.id())
            .orElseThrow(() -> new ForbiddenException("고유번호에 해당하는 사용자가 존재하지않습니다."));
    }
}
