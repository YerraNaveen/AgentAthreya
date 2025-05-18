package com.agentathreya.conversationservice.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.agentathreya.conversationservice.entity.ChatMessage;
import com.agentathreya.conversationservice.entity.ConversationRecord;
import com.agentathreya.conversationservice.repository.ChatRepository;
import com.agentathreya.conversationservice.repository.ConversationRecordRepository;
import com.agentathreya.conversationservice.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRepository chatRepository;
    
    private final ConversationRecordRepository conversationRecordRepository;
    
    private final LLMService llmService;

    public ChatMessage saveMessage(String userMessage) {
        String username = SecurityUtils.getAuthenticatedUsername();
        log.info("Saving user message: {}", userMessage);

        // Save user message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(username);
        chatMessage.setUserMessage(userMessage);
        chatMessage.setTimestamp(LocalDateTime.now());

        chatMessage = chatRepository.save(chatMessage);
        log.info("User message saved with ID: {}", chatMessage.getId());

        // Generate system response
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setSender(username);
//        systemMessage.setSystemMessage(systemResponse);
        systemMessage.setTimestamp(LocalDateTime.now());

        chatRepository.save(systemMessage);
        log.info("System message saved with ID: {}", systemMessage.getId());

        return chatMessage;
    }

    public void updateConversationWithResponse(UUID id, String aiResponse) {
        Optional<ConversationRecord> optionalRecord = conversationRecordRepository.findById(id);
        if (optionalRecord.isPresent()) {
            ConversationRecord record = optionalRecord.get();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("response", aiResponse);
            record.setResponse(responseMap);
            record.setStatus("RESPONSE_RECEIVED");
            record.setUpdatedAt(LocalDateTime.now());

            conversationRecordRepository.save(record);
        }
    }
    
    public ChatMessage saveAndRespond(ChatMessage userMessage) {

        // Generate system response
        String systemResponse = llmService.getLLMResponse(userMessage.getUserMessage());
        
        userMessage.setSystemMessage(systemResponse);

        // Save system response
        chatRepository.save(userMessage);

        return userMessage;
    }

    public List<ChatMessage> getChatHistory() {
    	log.info("Fetching full chat history");
        return chatRepository.findAll();
    }
}
