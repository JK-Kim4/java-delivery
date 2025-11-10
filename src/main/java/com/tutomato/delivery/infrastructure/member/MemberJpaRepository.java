package com.tutomato.delivery.infrastructure.member;

import com.tutomato.delivery.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByAccount(String account);
}
