package com.agentathreya.conversationservice.repository;

import com.agentathreya.conversationservice.entity.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationTypeRepository extends JpaRepository<ConversationType, Long> {
    Optional<ConversationType> findByTypeAndSubType(String type, String subType);
}
