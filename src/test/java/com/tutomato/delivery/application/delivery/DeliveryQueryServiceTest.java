package com.tutomato.delivery.application.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.tutomato.delivery.interfaces.delivery.dto.DeliverySearchCriteria;
import com.tutomato.delivery.application.delivery.dto.DeliverySearchResult;
import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.domain.delivery.Destination;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.order.Order;
import com.tutomato.delivery.infrastructure.delivery.DeliveryJpaRepository;
import com.tutomato.delivery.infrastructure.member.MemberJpaRepository;
import com.tutomato.delivery.infrastructure.order.OrderJpaRepository;
import com.tutomato.delivery.interfaces.delivery.dto.DeliverySearchPeriod;
import com.tutomato.delivery.supporter.MemberTestFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DeliveryQueryServiceTest {

    @Autowired
    private DeliveryQueryService deliveryQueryService;

    @Autowired
    private DeliveryJpaRepository deliveryJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("특정 라이더 + 상태 목록으로 기간 내 배달을 조회한다")
    void search_withRiderAndStatuses() {
        // given
        Member store = saveStoreMember();
        Member rider1 = saveRiderMember("rider1");
        Member rider2 = saveRiderMember("rider2");

        // rider1 - ASSIGNED
        Delivery d1 = saveDelivery(store, rider1, DeliveryStatus.ASSIGNED);

        // rider1 - COMPLETED
        Delivery d2 = saveDelivery(store, rider1, DeliveryStatus.COMPLETED);

        // rider2 - IN_DELIVERY
        Delivery d3 = saveDelivery(store, rider2, DeliveryStatus.IN_DELIVERY);

        // rider1 + [ASSIGNED, IN_DELIVERY] 로 검색
        DeliverySearchCriteria criteria = DeliverySearchCriteria.of(
            rider1.getId(), DeliverySearchPeriod.D3,
            List.of(DeliveryStatus.ASSIGNED, DeliveryStatus.IN_DELIVERY)
        );

        // when
        List<DeliverySearchResult> results = deliveryQueryService.search(criteria);

        // then
        assertThat(results)
            .extracting(DeliverySearchResult::deliveryId)
            .containsExactly(d1.getId());
    }

    @Test
    @DisplayName("riderId가 null이면 라이더와 상관없이 상태 조건만으로 조회한다")
    void search_withoutRider_filtersByStatusesOnly() {
        // given
        Member store = saveStoreMember();
        Member rider1 = saveRiderMember("rider1");
        Member rider2 = saveRiderMember("rider2");

        // COMPLETED 2건 (서로 다른 라이더)
        Delivery completed1 = saveDelivery(store, rider1, DeliveryStatus.COMPLETED);
        Delivery completed2 = saveDelivery(store, rider2, DeliveryStatus.COMPLETED);

        // 다른 상태 (REQUESTED)
        Delivery requested = saveDelivery(store, rider1, DeliveryStatus.REQUESTED);

        // riderId = null, statuses = [COMPLETED]
        DeliverySearchCriteria criteria = DeliverySearchCriteria.of(
            null, DeliverySearchPeriod.D3, List.of(DeliveryStatus.COMPLETED)
        );

        // when
        List<DeliverySearchResult> results = deliveryQueryService.search(criteria);

        // then
        assertThat(results)
            .extracting(DeliverySearchResult::deliveryId)
            .containsExactlyInAnyOrder(
                completed1.getId(),
                completed2.getId()
            );
    }

    @Test
    @DisplayName("상태 목록이 비어있으면 모든 상태의 배달을 조회한다")
    void search_withEmptyStatuses_returnsAllStatuses() {
        // given
        Member store = saveStoreMember();
        Member rider = saveRiderMember("rider1");

        Delivery d1 = saveDelivery(store, rider, DeliveryStatus.ASSIGNED);
        Delivery d2 = saveDelivery(store, rider, DeliveryStatus.IN_DELIVERY);
        Delivery d3 = saveDelivery(store, rider, DeliveryStatus.COMPLETED);

        // rider = rider, statuses = empty → rider 기준만 필터
        DeliverySearchCriteria criteria = DeliverySearchCriteria.of(
            rider.getId(), DeliverySearchPeriod.D1, List.of());

        // when
        List<DeliverySearchResult> results = deliveryQueryService.search(criteria);

        // then
        assertThat(results)
            .extracting(DeliverySearchResult::deliveryId)
            .containsExactlyInAnyOrder(
                d1.getId(), d2.getId(), d3.getId()
            );
    }

    @BeforeEach
    void setup() {
        memberJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
        deliveryJpaRepository.deleteAll();
    }

    // ======== 테스트용 helper 메서드들 ========

    private Member saveStoreMember() {
        Member store = new MemberTestFixture("store1", "가게1").toStoreMember();
        return memberJpaRepository.save(store);
    }

    private Member saveRiderMember(String account) {
        Member rider = new MemberTestFixture(account, "라이더-" + account).toRiderMember();
        return memberJpaRepository.save(rider);
    }

    private Delivery saveDelivery(
        Member store,
        Member rider,
        DeliveryStatus status
    ) {
        // Order 생성
        Order order = Order.create(
            store,
            "수신자-" + rider.getId(),
            "010-0000-0000"
        );
        orderJpaRepository.save(order);

        // Destination / Address 생성 (실제 구현에 맞게 교체)
        Destination destination = createDestination();

        // Delivery 생성
        Delivery delivery = Delivery.create(order, destination);

        // 상태에 맞게 도메인 메서드 호출
        // 기본: REQUESTED
        if (status == DeliveryStatus.ASSIGNED) {
            delivery.allocateTo(rider);
        } else if (status == DeliveryStatus.IN_DELIVERY) {
            delivery.allocateTo(rider);
            delivery.startDelivery();
        } else if (status == DeliveryStatus.COMPLETED) {
            delivery.allocateTo(rider);
            delivery.startDelivery();
            delivery.completeDelivery();
        } else if (status == DeliveryStatus.CANCELED) {
            // 필요하다면 order.cancel(...) + delivery 상태 변경 로직 추가
        }

        return deliveryJpaRepository.save(delivery);
    }

    private Destination createDestination() {
        return Destination.create(createAddress("상세 주소"));
    }

    private Address createAddress(String detail) {
        // 실제 Address 생성 방식에 맞게 수정
        return new Address("서울시 송파구", detail, "12345");
    }
}