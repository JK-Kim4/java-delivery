package com.tutomato.delivery.domain.order;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.tutomato.delivery.domain.BaseTimeEntity;
import com.tutomato.delivery.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private Member store;

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    @Column(name = "receiver_phone_number", nullable = false, length = 20)
    private String receiverPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "canceled_at", nullable = true)
    private Instant canceledAt;

    @Column(name = "cancel_reason", nullable = true)
    private String cancelReason;

    protected Order() {
    }

    private Order(Member store, String receiverName, String receiverPhoneNumber,
        OrderStatus orderStatus) {
        this.store = store;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.orderStatus = orderStatus;
    }

    public static Order create(Member store, String receiverName, String receiverPhoneNumber) {
        return new Order(store, receiverName, receiverPhoneNumber, OrderStatus.ORDER_RECEIVED);
    }

    public void startDelivery() {
        if (this.orderStatus != OrderStatus.ORDER_RECEIVED) {
            throw new IllegalStateException(
                "배송 시작은 주문 접수 상태에서만 가능합니다. 현재 상태: " + this.orderStatus);
        }
        this.orderStatus = OrderStatus.DELIVERY_STARTED;
    }

    public void completeDelivery() {
        if (this.orderStatus != OrderStatus.DELIVERY_STARTED) {
            throw new IllegalStateException(
                "배송 완료는 배송 시작 상태에서만 가능합니다. 현재 상태: " + this.orderStatus);
        }
        this.orderStatus = OrderStatus.DELIVERY_COMPLETED;
    }

    public void cancel(String reason) {
        if (this.orderStatus == OrderStatus.DELIVERY_COMPLETED) {
            throw new IllegalStateException("배송 완료된 주문은 취소할 수 없습니다.");
        }

        if (this.orderStatus == OrderStatus.DELIVERY_STARTED) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }

        if (this.orderStatus == OrderStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        this.orderStatus = OrderStatus.CANCELED;
        this.canceledAt = Instant.now();
        this.cancelReason = reason;
    }


    public Long getId() {
        return id;
    }

    public Member getStore() {
        return store;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Instant getCanceledAt() {
        return canceledAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }
}
