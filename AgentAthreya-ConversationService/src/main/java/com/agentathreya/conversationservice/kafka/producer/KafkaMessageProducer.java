package com.agentathreya.conversationservice.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.agentathreya.conversationservice.constants.AppConstants;
import com.agentathreya.conversationservice.entity.ConversationRecord;

@Service
public class KafkaMessageProducer {

   @Autowired
   private KafkaTemplate<String, ConversationRecord> kafkaTemplate;

   public void sendMessage(ConversationRecord message) {
       kafkaTemplate.send(AppConstants.TOPIC, message);
   }
}