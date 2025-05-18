package com.agentathreya.conversationservice.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.*;

@Immutable
@Entity
@Table(name = "conversation_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"type", "subType"})
})
public class ConversationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String subType;

    private String description;
}
