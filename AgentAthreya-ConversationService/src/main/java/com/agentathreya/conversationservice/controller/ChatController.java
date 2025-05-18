package com.agentathreya.conversationservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agentathreya.conversationservice.service.ConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
	    

    private final ConversationService conversationService;
    
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
    	conversationService.handleUserChat(message);
        return ResponseEntity.ok("Message sent to Kafka!");
    }

//    @PostMapping("/chat")
//    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage userMessage) {
//        try {
//            // Save the user message and generate a system response
//            ChatMessage systemMessage = chatService.saveAndRespond(userMessage);
//            return ResponseEntity.ok(systemMessage);
//        } catch (Exception e) {
//            logger.error("Error processing message", e);
//            return ResponseEntity.status(500).body(null);
//        }
//    }
//
//    @GetMapping("/history")
//    public List<ChatMessage> getChatHistory() {
//        logger.info("Fetching chat history...");
//        return chatService.getChatHistory();
//    }
//    

}