-- Member sample insert

-- RIDER
INSERT INTO members
    (id, account, "name", "password", "role", created_at, updated_at)
VALUES
    (1, 'rider', '라이더 테스트 계정','ec23120fd28152424f9f01ee29a543410b9dfb91f204ce5f4f6535e7ba15f513', 'RIDER','2025-11-15 00:00:00.000','2025-11-15 00:00:00.000');

-- STORE
INSERT INTO members
    (id, account, "name", "password", "role", created_at, updated_at)
VALUES
    (2, 'store', '가게 테스트 계정','088464644b277bf121437b47ed4216e5743c6ee8dc247b7709d1cca48cc70bfd', 'STORE','2025-11-15 00:00:00.000', '2025-11-15 00:00:00.000');


-- ORDER
-- ORDER
-- ORDER
INSERT INTO orders
(id, store_member_id, receiver_phone_number, receiver_name, order_status, created_at, updated_at)
VALUES
    -- 10분 전: 최근 주문 (ORDER_RECEIVED)
    (1, 2, '01012341234', '접수 주문1', 'ORDER_RECEIVED',
     now() - INTERVAL '10 minutes',
     now() - INTERVAL '10 minutes'),

    -- 40분 전: 30분~1시간 사이 (ORDER_RECEIVED)
    (2, 2, '01012341234', '접수 주문2', 'ORDER_RECEIVED',
     now() - INTERVAL '40 minutes',
     now() - INTERVAL '40 minutes'),

    -- 2시간 전
    (3, 2, '01012341234', '라이더 할당 전', 'ORDER_RECEIVED',
     now() - INTERVAL '2 hours',
     now() - INTERVAL '2 hours'),

    -- 5시간 전
    (4, 2, '01012341234', '라이더 할당 완료', 'DELIVERY_STARTED',
     now() - INTERVAL '5 hours',
     now() - INTERVAL '5 hours'),

    -- 11시간 전
    (5, 2, '01012341234', '배송 중', 'DELIVERY_STARTED',
     now() - INTERVAL '11 hours',
     now() - INTERVAL '11 hours'),

    -- 1일 5시간 전
    (6, 2, '01012341234', '배송 완료1', 'DELIVERY_COMPLETED',
     now() - INTERVAL '1 day 5 hours',
     now() - INTERVAL '1 day 5 hours'),

    -- 4일 전 (조회 범위 밖)
    (7, 2, '01012341234', '취소 주문', 'CANCELED',
     now() - INTERVAL '4 days',
     now() - INTERVAL '4 days');


-- DELIVERY
-- DELIVERY
-- DELIVERY
INSERT INTO deliveries
(order_id, rider_member_id,
 delivery_status,
 destination_zip_code, destination_address1, destination_address2,
 requested_at,
 allocated_at,
 delivery_stated_at,
 completed_at,
 created_at,
 updated_at)
VALUES
    -- (1) 주문1: 10분 전, REQUESTED (라이더 미할당)
    (1, null,
     'REQUESTED',
     '00123', '경기도', '고양시',
     now() - INTERVAL '10 minutes',   -- requested_at
     null,                            -- allocated_at
     null,                            -- delivery_stated_at
     null,                            -- completed_at
     now() - INTERVAL '10 minutes',   -- created_at
     now() - INTERVAL '10 minutes'),  -- updated_at

    -- (2) 주문2: 40분 전, REQUESTED (라이더 미할당)
    (2, null,
     'REQUESTED',
     '00543', '경기도', '하남시',
     now() - INTERVAL '40 minutes',   -- requested_at
     null,                            -- allocated_at
     null,                            -- delivery_stated_at
     null,                            -- completed_at
     now() - INTERVAL '40 minutes',
     now() - INTERVAL '40 minutes'),

    -- (3) 주문3: 2시간 전, REQUESTED (라이더 미할당, 나중에 ASSIGN 테스트용)
    (3, null,
     'REQUESTED',
     '00156', '서울특별시', '마포구',
     now() - INTERVAL '2 hours',
     null,
     null,
     null,
     now() - INTERVAL '2 hours',
     now() - INTERVAL '2 hours'),

    -- (4) 주문4: 5시간 전, ASSIGNED
    --   - 요청: 5시간 30분 전
    --   - 라이더 할당: 5시간 전
    (4, 1,
     'ASSIGNED',
     '00342', '서울특별시', '강남구',
     now() - INTERVAL '5 hours 30 minutes',   -- requested_at
     now() - INTERVAL '5 hours',              -- allocated_at
     null,                                    -- delivery_stated_at
     null,                                    -- completed_at
     now() - INTERVAL '5 hours 30 minutes',
     now() - INTERVAL '5 hours'),

    -- (5) 주문5: 11시간 전, IN_DELIVERY
    --   - 요청: 11시간 40분 전
    --   - 할당: 11시간 20분 전
    --   - 배송 시작: 11시간 전
    (5, 1,
     'IN_DELIVERY',
     '05534', '경기도', '용인시',
     now() - INTERVAL '11 hours 40 minutes',  -- requested_at
     now() - INTERVAL '11 hours 20 minutes',  -- allocated_at
     now() - INTERVAL '11 hours',             -- delivery_stated_at
     null,                                    -- completed_at
     now() - INTERVAL '11 hours 40 minutes',
     now() - INTERVAL '11 hours'),

    -- (6) 주문6: 1일 5시간 전, COMPLETED
    --   - 요청: 1일 6시간 전
    --   - 할당: 1일 5시간 30분 전
    --   - 배송 시작: 1일 5시간 전
    --   - 배송 완료: 1일 4시간 30분 전
    (6, 1,
     'COMPLETED',
     '00423', '서울특별시', '강서구',
     now() - INTERVAL '1 day 6 hours',        -- requested_at
     now() - INTERVAL '1 day 5 hours 30 minutes', -- allocated_at
     now() - INTERVAL '1 day 5 hours',        -- delivery_stated_at
     now() - INTERVAL '1 day 4 hours 30 minutes', -- completed_at
     now() - INTERVAL '1 day 6 hours',
     now() - INTERVAL '1 day 4 hours 30 minutes'),

    -- (7) 주문7: 4일 전, CANCELED (조회 최대 3일 밖 데이터)
    --   - 요청: 4일 1시간 전
    --   - 나머지는 null (취소 로직은 Order 쪽 canceled_at 에서 처리한다고 가정)
    (7, 1,
     'CANCELED',
     '005534', '서울특별시', '중구',
     now() - INTERVAL '4 days 1 hour',  -- requested_at
     null,
     null,
     null,
     now() - INTERVAL '4 days 1 hour',
     now() - INTERVAL '4 days 1 hour');
