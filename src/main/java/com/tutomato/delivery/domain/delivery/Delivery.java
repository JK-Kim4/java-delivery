package com.tutomato.delivery.domain.delivery;


import com.tutomato.delivery.domain.BaseTimeEntity;
import com.tutomato.delivery.domain.delivery.exception.IllegalRiderException;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.exception.IllegalMemberRoleException;
import com.tutomato.delivery.domain.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "deliveries")
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_member_id")
    private Member rider;

    @Embedded
    private Destination destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "allocated_at")
    private Instant allocatedAt;

    @Column(name = "delivery_stated_at")
    private Instant deliveryStartedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    protected Delivery() {
    }

    private Delivery(
        Order order,
        Destination destination,
        DeliveryStatus deliveryStatus,
        Instant requestedAt
    ) {
        this.order = order;
        this.destination = destination;
        this.deliveryStatus = deliveryStatus;
        this.requestedAt = requestedAt;
    }

    public static Delivery create(
        Order order,
        Destination destination
    ) {
        return new Delivery(order, destination, DeliveryStatus.REQUESTED, Instant.now());
    }

    public void allocateTo(Member rider) {
        this.rider = rider;
        this.allocatedAt = Instant.now();
        this.deliveryStatus = DeliveryStatus.ASSIGNED;
    }

    public void startDelivery() {
        if (deliveryStatus != DeliveryStatus.ASSIGNED) {
            throw new IllegalStateException("라이더 할당 완료 상태의 배달만 배달은 시작할 수 있습니다.");
        }

        this.deliveryStatus = DeliveryStatus.IN_DELIVERY;
        this.deliveryStartedAt = Instant.now();
        this.order.startDelivery();
    }

    public void completeDelivery() {
        if (deliveryStatus != DeliveryStatus.IN_DELIVERY) {
            throw new IllegalStateException("배송중인 배달만 배달 완료할 수 있습니다.");
        }

        this.deliveryStatus = DeliveryStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.order.completeDelivery();
    }

    public void updateDestination(Address address) {

        if (!deliveryStatus.isDestinationChangeAllowed()) {
            throw new IllegalStateException("픽업이 완료되어 도착지를 변경할 수 없습니다.");
        }

        this.destination = Destination.create(address);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Member getRider() {
        return rider;
    }

    public Destination getDestination() {
        return destination;
    }

    public Address getDestinationAddress() {
        return destination.getAddress();
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getAllocatedAt() {
        return allocatedAt;
    }

    public Instant getDeliveryStartedAt() {
        return deliveryStartedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void validateRider(Member rider) {
        if (!Objects.equals(this.rider, rider)) {
            throw new IllegalMemberRoleException("배달을 할당받은 라이더 정보가 일치하지않습니다.");
        }
    }

    public void validateStore(Member store) {
        if (!Objects.equals(this.order.getStore(), store)) {
            throw new IllegalMemberRoleException("주문 정보가 일치하지 않습니다.");
        }
    }
}
