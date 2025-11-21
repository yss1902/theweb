package com.theweb.theweb.repository;


import com.theweb.theweb.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"roles", "roles.role"})
    Optional<Member> findByUsername(String username);
}
