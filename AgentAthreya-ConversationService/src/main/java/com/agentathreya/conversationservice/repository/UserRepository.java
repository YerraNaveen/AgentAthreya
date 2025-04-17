package com.agentathreya.conversationservice.repository;

import com.agentathreya.conversationservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
