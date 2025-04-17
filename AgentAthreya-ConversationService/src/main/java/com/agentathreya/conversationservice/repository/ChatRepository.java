package com.agentathreya.conversationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agentathreya.conversationservice.entity.ChatMessage;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
}
