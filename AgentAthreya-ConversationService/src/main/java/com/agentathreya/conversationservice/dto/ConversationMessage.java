package com.agentathreya.conversationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessage {
    private UUID requestId;
    private UUID userId;
    private String type;
    private String subType;
    private Map<String, Object> payload;
}
