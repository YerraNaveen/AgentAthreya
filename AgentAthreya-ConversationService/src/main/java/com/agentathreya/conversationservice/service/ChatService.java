package com.agentathreya.conversationservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agentathreya.conversationservice.entity.ChatMessage;
import com.agentathreya.conversationservice.repository.ChatRepository;
import com.agentathreya.conversationservice.utils.SecurityUtils;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LLMService llmService;

    public ChatMessage saveMessage(String userMessage) {
        String username = SecurityUtils.getAuthenticatedUsername();
        logger.info("Saving user message: {}", userMessage);

        // Save user message
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(username)
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();

        chatMessage = chatRepository.save(chatMessage);
        logger.info("User message saved with ID: {}", chatMessage.getId());

        // Generate system response
        String systemResponse = llmService.getLLMResponse(userMessage);
        ChatMessage systemMessage = ChatMessage.builder()
                .sender(username)
                .systemMessage(systemResponse)
                .timestamp(LocalDateTime.now())
                .build();

        chatRepository.save(systemMessage);
        logger.info("System message saved with ID: {}", systemMessage.getId());

        return chatMessage;
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
        logger.info("Fetching full chat history");
        return chatRepository.findAll();
    }
}
