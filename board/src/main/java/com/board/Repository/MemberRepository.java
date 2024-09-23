package com.board.Repository;

import com.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    String findById(String id);
    Optional<Member> findByIdentity(String identity);
}
