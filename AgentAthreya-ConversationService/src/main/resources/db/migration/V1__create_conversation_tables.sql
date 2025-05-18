-- Enable UUID & JSONB extensions if not already present
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Table: conversation_type
CREATE TABLE conversation_type (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    sub_type VARCHAR(50) NOT NULL,
    description TEXT,
    UNIQUE (type, sub_type)
);

-- 2. Table: conversation_templates
CREATE TABLE conversation_templates (
    id SERIAL PRIMARY KEY,
    conversation_type_id INTEGER NOT NULL REFERENCES conversation_type(id),
    template JSONB NOT NULL,
    version INTEGER NOT NULL DEFAULT 1
);

-- 3. Table: conversation_records
CREATE TABLE conversation_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    request_id UUID NOT NULL,
    user_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    sub_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUEST_SUBMITTED',
    request JSONB NOT NULL,
    response JSONB,
    payload JSONB NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    retry_allowed BOOLEAN NOT NULL DEFAULT true,
    error_details JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes
CREATE INDEX idx_conversation_type_type_subtype ON conversation_type (type, sub_type);
CREATE INDEX idx_conversation_records_request_id ON conversation_records (request_id);
CREATE INDEX idx_conversation_records_user_id ON conversation_records (user_id);
CREATE INDEX idx_conversation_records_payload ON conversation_records USING GIN (payload);
