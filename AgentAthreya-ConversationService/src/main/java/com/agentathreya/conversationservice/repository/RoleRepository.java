package com.agentathreya.conversationservice.repository;

import com.agentathreya.conversationservice.entity.Role;
import com.agentathreya.conversationservice.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
