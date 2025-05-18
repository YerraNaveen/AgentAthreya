package com.agentathreya.conversationservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class LLMService {
	
	private static final Logger logger = LoggerFactory.getLogger(LLMService.class);

    @Value("${llm.api.url}")
    private String llmApiUrl;

    @Value("${llm.api.key}")
    private String llmApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    
    @Async
    public String getLLMResponse(String userMessage) {
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(llmApiKey);

            // Build the request body using maps (no need to manually escape JSON)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", userMessage);
            messages.add(message);

            requestBody.put("messages", messages);

            // Create the request
            HttpEntity<String> entity = new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    headers
            );

            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    llmApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parse the response
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode messageContent = jsonNode.path("choices").get(0).path("message").path("content");

            return messageContent != null ? messageContent.asText() : "No response received.";

        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Rate limit exceeded or quota issue: {}", e.getResponseBodyAsString());
            return "Request limit exceeded. Please try again later.";
        } catch (Exception e) {
            logger.error("Error while sending request to OpenAI API", e);
            return "Sorry, I couldn't process your request.";
        }
    }


    @Value("${llm.api.key}")
    private String apiKey;

    @PostConstruct
    public void printApiKey() {
        System.out.println("API Key Loaded: " + apiKey);
    }

}
