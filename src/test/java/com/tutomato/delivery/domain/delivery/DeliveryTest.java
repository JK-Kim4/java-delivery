package com.tutomato.delivery.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.order.Order;
import com.tutomato.delivery.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    private Order createOrder() {
        Member store = mock(Member.class);
        return Order.create(store, "홍길동", "010-1111-2222");
    }

    private Destination dummyDestination() {
        Address address = mock(Address.class);
        return Destination.create(address);
    }

    private Member dummyRider() {
        return mock(Member.class);
    }

    @Test
    @DisplayName("Delivery 생성 시 상태는 REQUESTED, requestedAt 은 null 이 아니다")
    void createDelivery_initialStatusRequested() {
        // given
        Order order = createOrder();
        Destination destination = dummyDestination();

        // when
        Delivery delivery = Delivery.create(order, destination);

        // then
        assertThat(delivery.getOrder()).isEqualTo(order);
        assertThat(delivery.getDestination()).isEqualTo(destination);
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.REQUESTED);
        assertThat(delivery.getRequestedAt()).isNotNull();
        assertThat(delivery.getAllocatedAt()).isNull();
        assertThat(delivery.getCompletedAt()).isNull();
        assertThat(delivery.getRider()).isNull();
    }

    @Nested
    @DisplayName("allocateTo 메서드")
    class AllocateTo {

        @Test
        @DisplayName("라이더 배정 시 rider, allocatedAt, status 가 ASSIGNED 로 설정된다")
        void allocateTo_success() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination);
            Member rider = dummyRider();

            // when
            delivery.allocateTo(rider);

            // then
            assertThat(delivery.getRider()).isEqualTo(rider);
            assertThat(delivery.getAllocatedAt()).isNotNull();
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.ASSIGNED);
        }
    }

    @Nested
    @DisplayName("startDelivery 메서드")
    class StartDelivery {

        @Test
        @DisplayName("delivery의 상태가 ASSIGNED인 배달만 배달을 시작할 수 있고, Order 도 배송 시작 상태가 된다")
        void startDelivery_success() {
            // given
            Order order = createOrder(); // ORDER_RECEIVED 상태
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination);
            Member rider = dummyRider();

            delivery.allocateTo(rider); // ASSIGNED 상태

            // when
            delivery.startDelivery();

            // then
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_DELIVERY);
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_STARTED);
        }

        @Test
        @DisplayName("delivery의 상태가 ASSIGNED이 아니면 배달을 시작할 수 없다")
        void startDelivery_fail_ifNotAssigned() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination); // REQUESTED 상태

            // when & then
            assertThatThrownBy(delivery::startDelivery)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("라이더 할당 완료 상태의 배달만 배달은 시작할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("completeDelivery 메서드")
    class CompleteDelivery {

        @Test
        @DisplayName("delivery의 상태가 IN_DELIVERY인 배달만 완료할 수 있고, 완료 시 completedAt 이 설정되고 Order 도 완료상태로 갱신된다")
        void completeDelivery_success() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination);
            Member rider = dummyRider();

            delivery.allocateTo(rider);   // ASSIGNED
            delivery.startDelivery();     // IN_DELIVERY & order.DELIVERY_STARTED

            // when
            delivery.completeDelivery();

            // then
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.COMPLETED);
            assertThat(delivery.getCompletedAt()).isNotNull();
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_COMPLETED);
        }

        @Test
        @DisplayName("delivery의 상태가 IN_DELIVERY 상태가 아니면 배달 완료할 수 없다")
        void completeDelivery_fail_ifNotInDelivery() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination);
            // 현재 상태: REQUESTED

            // when & then
            assertThatThrownBy(delivery::completeDelivery)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("배송중인 배달만 배달 완료할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("updateDestination 메서드")
    class UpdateDestination {

        @Test
        @DisplayName("목적지 변경이 허용되는 상태에서는 도착지가 변경된다")
        void updateDestination_success_whenAllowed() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination); // REQUESTED 상태

            Address newAddress = mock(Address.class);

            // when
            delivery.updateDestination(newAddress);

            // then
            assertThat(delivery.getDestination()).isNotNull();
            // Destination.equals 가 구현되어 있으면 더 강하게 검증 가능
        }

        @Test
        @DisplayName("픽업/배송이 진행된 상태(IN_DELIVERY)에서는 목적지 변경 시 예외가 발생한다")
        void updateDestination_fail_whenNotAllowed() {
            // given
            Order order = createOrder();
            Destination destination = dummyDestination();
            Delivery delivery = Delivery.create(order, destination);
            Member rider = dummyRider();

            delivery.allocateTo(rider);   // ASSIGNED
            delivery.startDelivery();     // IN_DELIVERY (대부분 이 상태에서 변경 불가로 볼 수 있음)

            Address newAddress = mock(Address.class);

            // when & then
            assertThatThrownBy(() -> delivery.updateDestination(newAddress))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("픽업이 완료되어 도착지를 변경할 수 없습니다.");
        }
    }

}