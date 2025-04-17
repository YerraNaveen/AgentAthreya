package com.agentathreya.conversationservice.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private static final ConcurrentHashMap<String, String> sessionStore = new ConcurrentHashMap<>();

    public static void setSessionAttribute(String key, String value) {
        sessionStore.put(key, value);
    }

    public static String getSessionAttribute(String key) {
        return sessionStore.get(key);
    }

    public static void removeSessionAttribute(String key) {
        sessionStore.remove(key);
    }

    public static void clearSession() {
        sessionStore.clear();
    }
}