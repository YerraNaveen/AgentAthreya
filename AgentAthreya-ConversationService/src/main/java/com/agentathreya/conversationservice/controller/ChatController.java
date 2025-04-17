package com.agentathreya.conversationservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agentathreya.conversationservice.entity.ChatMessage;
import com.agentathreya.conversationservice.service.ChatService;

@RestController
@RequestMapping("/api/v1/conversation")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage userMessage) {
        try {
            // Save the user message and generate a system response
            ChatMessage systemMessage = chatService.saveAndRespond(userMessage);
            return ResponseEntity.ok(systemMessage);
        } catch (Exception e) {
            logger.error("Error processing message", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory() {
        logger.info("Fetching chat history...");
        return chatService.getChatHistory();
    }
}
