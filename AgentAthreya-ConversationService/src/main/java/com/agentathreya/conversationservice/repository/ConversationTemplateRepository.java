package com.agentathreya.conversationservice.repository;

import com.agentathreya.conversationservice.entity.ConversationTemplate;
import com.agentathreya.conversationservice.entity.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationTemplateRepository extends JpaRepository<ConversationTemplate, Long> {
    Optional<ConversationTemplate> findByConversationTypeAndVersion(ConversationType type, Integer version);
}
