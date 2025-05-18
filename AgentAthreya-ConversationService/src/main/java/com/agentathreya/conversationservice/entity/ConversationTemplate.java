package com.agentathreya.conversationservice.entity;

import java.util.Map;

import org.hibernate.annotations.Immutable;

import com.agentathreya.conversationservice.utils.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Immutable
@Entity
@Table(name = "conversation_templates")
public class ConversationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_type_id", nullable = false)
    private ConversationType conversationType;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> template;

    private Integer version;
}
