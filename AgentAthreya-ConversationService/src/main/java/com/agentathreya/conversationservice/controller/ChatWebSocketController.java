package com.agentathreya.conversationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.agentathreya.conversationservice.entity.ChatMessage;
import com.agentathreya.conversationservice.service.ChatService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage userMessage) {
        messagingTemplate.convertAndSend("/topic/messages", userMessage);

        // Save & get LLM response
        ChatMessage systemMessage = chatService.saveAndRespond(userMessage);

        // Broadcast LLM response
        messagingTemplate.convertAndSend("/topic/messages", systemMessage);
    }
}
