package com.tutomato.delivery.application.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.tutomato.delivery.application.delivery.dto.DeliveryStatusUpdateCommand;
import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.domain.delivery.Destination;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.order.Order;
import com.tutomato.delivery.domain.order.OrderStatus;
import com.tutomato.delivery.infrastructure.delivery.DeliveryJpaRepository;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import com.tutomato.delivery.infrastructure.order.OrderJpaRepository;
import com.tutomato.delivery.supporter.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DeliveryUpdateServiceIntegrationTest {

    @Autowired
    DeliveryUpdateService deliveryUpdateService;

    @Autowired
    DeliveryJpaRepository deliveryJpaRepository;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    EntityManager em;

    private Member createStoreMember(String account) {
        Member member = new MemberTestFixture(account, "가게").toStoreMember();
        return memberJpaRepository.save(member);
    }

    private Member createRiderMember(String account) {
        Member member = new MemberTestFixture(account, "라이더").toRiderMember();
        return memberJpaRepository.save(member);
    }

    private Order createOrder(Member store) {
        Order order = Order.create(store, "홍길동", "010-1111-2222");
        return orderJpaRepository.save(order);
    }

    private Delivery createRequestedDelivery(Order order, Address address) {
        Delivery delivery = Delivery.create(order, Destination.create(address));
        return deliveryJpaRepository.save(delivery);
    }

    private Address createAddress(String detail) {
        // 실제 Address 생성 방식에 맞게 수정
        return new Address("서울시 송파구", detail, "12345");
    }

    @Nested
    @DisplayName("startDelivery")
    class StartDeliveryTests {

        @Test
        @DisplayName("가게 회원이 자신의 주문 배달을 시작하면 Delivery/Order 상태가 함께 변경된다")
        @Rollback
        void startDelivery_success() {
            // given
            Member store = createStoreMember("store01");
            Member rider = createRiderMember("rider01");
            Order order = createOrder(store);
            Delivery delivery = createRequestedDelivery(order, createAddress("기존 주소"));

            // 라이더 배정 (ASSIGNED 상태)
            delivery.allocateTo(rider);
            em.flush();
            em.clear();

            DeliveryStatusUpdateCommand command = DeliveryStatusUpdateCommand.createWithStoreId(
                delivery.getId(), store.getId()
            );

            // when
            deliveryUpdateService.startDelivery(command);
            em.flush();
            em.clear();

            // then
            Delivery updatedDelivery = deliveryJpaRepository.findById(delivery.getId())
                .orElseThrow();
            Order updatedOrder = orderJpaRepository.findById(order.getId()).orElseThrow();

            assertThat(updatedDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_DELIVERY);
            assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_STARTED);
        }
    }

    @Nested
    @DisplayName("completeDelivery")
    class CompleteDeliveryTests {

        @Test
        @DisplayName("라이더가 배달 완료 처리 시 Delivery/Order 상태가 함께 완료로 변경된다")
        @Rollback
        void completeDelivery_success() {
            // given
            Member store = createStoreMember("store02");
            Member rider = createRiderMember("rider02");
            Order order = createOrder(store);
            Delivery delivery = createRequestedDelivery(order, createAddress("기존 주소"));

            // 배달 할당 및 시작까지 진행
            delivery.allocateTo(rider);
            delivery.startDelivery();
            em.flush();
            em.clear();

            DeliveryStatusUpdateCommand command = DeliveryStatusUpdateCommand.createWithRiderId(
                delivery.getId(), rider.getId());

            // when
            deliveryUpdateService.completeDelivery(command);
            em.flush();
            em.clear();

            // then
            Delivery updatedDelivery = deliveryJpaRepository.findById(delivery.getId())
                .orElseThrow();
            Order updatedOrder = orderJpaRepository.findById(order.getId()).orElseThrow();

            assertThat(updatedDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.COMPLETED);
            assertThat(updatedDelivery.getCompletedAt()).isNotNull();
            assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_COMPLETED);
        }
    }

    @Nested
    @DisplayName("modifyAddress")
    class ModifyAddressTests {

        @Test
        @DisplayName("가게 회원이 자신 가게의 주문에 대해 도착지를 변경할 수 있다 (변경 허용 상태일 때)")
        @Rollback
        void modifyAddress_success() {
            // given
            Member store = createStoreMember("store03");
            Order order = createOrder(store);
            Delivery delivery = createRequestedDelivery(order, createAddress("원래 주소"));
            em.flush();
            em.clear();

            Address newAddress = createAddress("변경된 주소");

            DeliveryAddressModifyCommand command = new DeliveryAddressModifyCommand(
                delivery.getId(),
                store.getId(),
                newAddress
            );

            // when
            deliveryUpdateService.modifyAddress(command);
            em.flush();
            em.clear();

            // then
            Delivery updatedDelivery = deliveryJpaRepository.findById(delivery.getId())
                .orElseThrow();

            assertThat(updatedDelivery.getDestination().getAddress())
                .satisfies(addr -> {
                    assertThat(addr).isEqualTo(newAddress);
                });
            assertThat(updatedDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.REQUESTED);
        }
    }
}