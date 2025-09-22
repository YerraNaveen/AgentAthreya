package com.agentathreya.conversationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgentAthreyaConversationServiceApplication  implements CommandLineRunner {

    @Value("${jwt.secret:NOT_LOADED}")  // Checks 'jwt.secret' from .env/application.yml
    private String jwtSecret;


    @Value("${DB_URL:NOT_LOADED}")
    private String dbUrl;

    public static void main(String[] args) {
        SpringApplication.run(AgentAthreyaConversationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== ENV PROPERTY CHECK ===");
        System.out.println("JWT_SECRET loaded: " + (jwtSecret.startsWith("NOT_LOADED") ? "❌" : "✅ (value hidden for security)"));
        System.out.println("DB_URL loaded: " + (dbUrl.startsWith("NOT_LOADED") ? "❌" : "✅ " + dbUrl));
        System.out.println("=======================\n");
    }
}
