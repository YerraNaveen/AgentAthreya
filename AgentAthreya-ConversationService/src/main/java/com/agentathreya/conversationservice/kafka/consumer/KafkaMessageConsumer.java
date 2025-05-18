package com.agentathreya.conversationservice.kafka.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.agentathreya.conversationservice.constants.AppConstants;
import com.agentathreya.conversationservice.entity.ConversationRecord;
import com.agentathreya.conversationservice.service.ChatService;
import com.agentathreya.conversationservice.service.LLMService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {
	
   private final ExecutorService executor = Executors.newCachedThreadPool();
   private final SimpMessagingTemplate messagingTemplate;
   private final ChatService conversationService;
   private final LLMService llmService;
//   private final ChatClient chatClient;

   @KafkaListener(topics = AppConstants.TOPIC, groupId = AppConstants.KAFKA_GROUP_ID)
   public void consume(ConversationRecord message) {
   	executor.submit(() -> handleMessage(message));
   }
   
   @Async
   private void handleMessage(ConversationRecord record) {
   	try {
           // 1. Call AI API
           CompletableFuture<String> aiResponseFuture = CompletableFuture.supplyAsync(
           		() -> llmService.getLLMResponse((String) record.getRequest().get("Message")), 
           		executor);

           // 2. Update DB and 3. Push to WebSocket in parallel after LLM response
           aiResponseFuture.thenAccept(aiResponse -> {
                CompletableFuture.runAsync(() ->
                    conversationService.updateConversationWithResponse(record.getRequestId(), aiResponse), executor);

               CompletableFuture.runAsync(() ->
                   messagingTemplate.convertAndSend("/topic/messages", aiResponse), executor);

               log.info("Processed requestId: {}", record.getRequestId());
           }).exceptionally(ex -> {
               log.error("Error in AI processing or parallel tasks", ex);
               return null;
           });

       } catch (Exception e) {
           log.error("Error handling message {}", record.getRequestId(), e);
       }
   }
   
//   String[] describe() {
//	   UserMessage um = new UserMessage("Explain what do you see on each image.",
//	            List.copyOf(Stream.concat(images.stream(), dynamicImages.stream()).toList()));
//	   
//	      return this.chatClient.prompt(new Prompt("Explain what do you see on each image."))
//	              .call()
//	              .entity(String[].class);
//	}
}
