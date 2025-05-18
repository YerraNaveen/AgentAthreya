package com.agentathreya.conversationservice.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.agentathreya.conversationservice.entity.ConversationRecord;
import com.agentathreya.conversationservice.kafka.producer.KafkaMessageProducer;
import com.agentathreya.conversationservice.repository.ConversationRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

    private KafkaMessageProducer kafkaProducer;
    private final ConversationRecordRepository recordRepository;

    public void handleUserChat(String chatMessage) {
        UUID requestId = UUID.randomUUID();
        UUID userId = UUID.randomUUID(); // Replace with real user session later

        Map<String, Object> payload = new HashMap<>();
        payload.put("message", chatMessage);

        ConversationRecord record = ConversationRecord.builder()
                .requestId(requestId)
                .userId(userId)
                .type("CHAT")
                .subType("USER_MESSAGE")
                .status("REQUEST_SUBMITTED")
                .retryCount(0)
                .retryAllowed(true)
                .request(payload)
                .response(new HashMap<>())
                .errorDetails(new HashMap<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        log.info("Record", record);
        ConversationRecord dbRecord = recordRepository.save(record);
        record.setId(dbRecord.getId());
        kafkaProducer.sendMessage(record);
    }
} 