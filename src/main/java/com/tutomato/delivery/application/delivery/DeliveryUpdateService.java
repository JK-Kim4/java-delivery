package com.tutomato.delivery.application.delivery;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyCommand;
import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyResult;
import com.tutomato.delivery.application.delivery.dto.DeliveryStatusUpdateCommand;
import com.tutomato.delivery.application.delivery.dto.DeliveryStatusUpdateResult;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.exception.DeliveryNotFoundException;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.exception.MemberNotFoundException;
import com.tutomato.delivery.infrastructure.delivery.DeliveryJpaRepository;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryUpdateService {

    private final MemberJpaRepository memberJpaRepository;
    private final DeliveryJpaRepository deliveryJpaRepository;

    public DeliveryUpdateService(
        MemberJpaRepository memberJpaRepository,
        DeliveryJpaRepository deliveryJpaRepository
    ) {
        this.deliveryJpaRepository = deliveryJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    public DeliveryStatusUpdateResult startDelivery(DeliveryStatusUpdateCommand command) {
        // 1. 배송 조회
        Delivery delivery = deliveryJpaRepository.findById(command.deliveryId())
            .orElseThrow(() -> new DeliveryNotFoundException("배달을 조회할 수 없습니다."));

        // 2. 사용자 조회
        Member member = memberJpaRepository.findById(command.storeMemberId())
            .orElseThrow(() -> new MemberNotFoundException("사용자를 조회할 수 없습니다."));

        // 3. 주문 가게 검증
        delivery.validateStore(member);

        // 4. 배송 시작
        delivery.startDelivery();

        // 5. 반환
        return DeliveryStatusUpdateResult.from(delivery);
    }

    public DeliveryStatusUpdateResult completeDelivery(DeliveryStatusUpdateCommand command) {
        // 1. 배송 조회
        Delivery delivery = deliveryJpaRepository.findById(command.deliveryId())
            .orElseThrow(() -> new DeliveryNotFoundException("배달을 조회할 수 없습니다."));

        // 2. 사용자 조회
        Member member = memberJpaRepository.findById(command.riderMemberId())
            .orElseThrow(() -> new MemberNotFoundException("사용자를 조회할 수 없습니다."));

        // 3. 할당 라이더 검증
        delivery.validateRider(member);

        // 4. 배송 완료 처리
        delivery.completeDelivery();

        // 5. 반환
        return DeliveryStatusUpdateResult.from(delivery);
    }

    public DeliveryAddressModifyResult modifyAddress(DeliveryAddressModifyCommand command) {
        // 1. 배송 조회
        Delivery delivery = deliveryJpaRepository.findById(command.deliveryId())
            .orElseThrow(() -> new DeliveryNotFoundException("배달을 조회할 수 없습니다."));

        // 2. 사용자 조회
        Member member = memberJpaRepository.findById(command.storeMemberId())
            .orElseThrow(() -> new MemberNotFoundException("사용자를 조회할 수 없습니다."));

        // 3. 주문 가게 검증
        delivery.validateStore(member);

        // 2. 주소 변경
        delivery.updateDestination(command.newAddress());

        // 3. 반환
        return DeliveryAddressModifyResult.from(delivery);
    }
}
