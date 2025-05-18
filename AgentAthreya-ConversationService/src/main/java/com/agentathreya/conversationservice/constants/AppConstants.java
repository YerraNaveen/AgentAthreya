package com.agentathreya.conversationservice.constants;

public final class AppConstants {

    private AppConstants() {
        // prevent instantiation
    }

    // 🔌 Kafka
    public static final String CONVERSATION_TOPIC = "conversation-topic";
    public static final String KAFKA_GROUP_ID = "agentathreya-group";

    // 📦 Status values
    public static final String STATUS_REQUEST_SUBMITTED = "REQUEST_SUBMITTED";
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_FAILED = "FAILED";

    // 🔁 Retry
    public static final boolean RETRY_ALLOWED_DEFAULT = true;
    public static final int DEFAULT_RETRY_COUNT = 0;

    // ⏰ DateTime format (optional)
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static final String TOPIC = "conversation-topic";
}
