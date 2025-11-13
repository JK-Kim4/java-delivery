package com.tutomato.delivery.application.delivery;

import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateCommand;
import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateResult;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.domain.delivery.exception.DeliveryNotFoundException;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.exception.IllegalMemberRoleException;
import com.tutomato.delivery.domain.member.exception.MemberNotFountException;
import com.tutomato.delivery.infrastructure.delivery.DeliveryJpaRepository;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryAllocateService {

    private final MemberJpaRepository memberJpaRepository;
    private final DeliveryJpaRepository deliveryJpaRepository;

    public DeliveryAllocateService(
        MemberJpaRepository memberJpaRepository,
        DeliveryJpaRepository deliveryJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.deliveryJpaRepository = deliveryJpaRepository;
    }

    public DeliveryAllocateResult allocate(DeliveryAllocateCommand command) {

        // 1. 라이더 검증
        Member rider = memberJpaRepository.findById(command.riderMemberId())
            .orElseThrow(() -> new MemberNotFountException("사용자를 조회할 수 없습니다."));

        if (!rider.isRiderMember()) {
            throw new IllegalMemberRoleException("라이더에 해당하는 회원이 아닙니다.");
        }

        // 2. 배달 조회
        Delivery delivery = deliveryJpaRepository.findById(command.deliveryId())
            .orElseThrow(() -> new DeliveryNotFoundException("배달을 조회할 수 없습니다."));

        // 3. 라이더 할당
        delivery.allocateTo(rider);

        // 4. 반환
        return DeliveryAllocateResult.of(rider, delivery);
    }

}
