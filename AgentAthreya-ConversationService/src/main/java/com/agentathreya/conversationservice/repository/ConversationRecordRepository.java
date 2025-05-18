package com.agentathreya.conversationservice.repository;

import com.agentathreya.conversationservice.entity.ConversationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationRecordRepository extends JpaRepository<ConversationRecord, UUID> {
}
