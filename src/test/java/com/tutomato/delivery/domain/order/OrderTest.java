package com.tutomato.delivery.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.order.exception.IllegalOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Member dummyStore() {
        // Member 생성 로직은 이 테스트의 관심사가 아니므로 mock 으로 대체
        return mock(Member.class);
    }

    @Test
    @DisplayName("주문 생성 시 기본 상태는 ORDER_RECEIVED 이다")
    void create_order_defaultStatus_ORDER_RECEIVED() {
        // given
        Member store = dummyStore();

        // when
        Order order = Order.create(store, "홍길동", "010-1111-2222");

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER_RECEIVED);
    }

    @Nested
    @DisplayName("startDelivery 메서드")
    class StartDelivery {

        @Test
        @DisplayName("주문 접수 상태에서 배송 시작 시 상태가 DELIVERY_STARTED 로 변경된다")
        void startDelivery_success() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");

            // when
            order.startDelivery();

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_STARTED);
        }

        @Test
        @DisplayName("주문 접수 상태가 아니면 배송 시작 시 예외가 발생한다")
        void startDelivery_fail_ifNotOrderReceived() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            order.startDelivery(); // DELIVERY_STARTED 상태로 변경

            // when & then
            assertThatThrownBy(order::startDelivery)
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessageContaining("배송 시작은 주문 접수 상태에서만 가능합니다");
        }
    }

    @Nested
    @DisplayName("completeDelivery 메서드")
    class CompleteDelivery {

        @Test
        @DisplayName("배송 시작 상태에서 배송 완료 시 상태가 DELIVERY_COMPLETED 로 변경된다")
        void completeDelivery_success() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            order.startDelivery(); // DELIVERY_STARTED

            // when
            order.completeDelivery();

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY_COMPLETED);
        }

        @Test
        @DisplayName("배송 시작 상태가 아니면 배송 완료 시 예외가 발생한다")
        void completeDelivery_fail_ifNotDeliveryStarted() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            // 아직 ORDER_RECEIVED 상태

            // when & then
            assertThatThrownBy(order::completeDelivery)
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessageContaining("배송 완료는 배송 시작 상태에서만 가능합니다");
        }
    }

    @Nested
    @DisplayName("cancel 메서드")
    class Cancel {

        @Test
        @DisplayName("배송 완료 전 상태의 주문은 취소할 수 있다")
        void cancel_success_beforeCompleted() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            String reason = "악천우";
            // ORDER_RECEIVED 상태에서 바로 취소

            // when
            order.cancel(reason);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
            assertThat(order.getCancelReason()).isEqualTo(reason);
        }

        @Test
        @DisplayName("배송 완료된 주문은 취소할 수 없다")
        void cancel_fail_ifCompleted() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            String reason = "단순 변심";
            order.startDelivery();
            order.completeDelivery(); // DELIVERY_COMPLETED

            // when & then
            assertThatThrownBy(() -> order.cancel(reason))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessageContaining("배송 완료된 주문은 취소할 수 없습니다");
        }

        @Test
        @DisplayName("이미 취소된 주문은 다시 취소할 수 없다")
        void cancel_fail_ifAlreadyCanceled() {
            // given
            Member store = dummyStore();
            Order order = Order.create(store, "홍길동", "010-1111-2222");
            String reason = "단순 변심";
            order.cancel(reason);

            // when & then
            assertThatThrownBy(() -> order.cancel(reason))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessageContaining("이미 취소된 주문입니다");
        }
    }
}