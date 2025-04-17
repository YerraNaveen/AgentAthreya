package com.agentathreya.conversationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agentathreya.conversationservice.entity.UploadedFile;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
}
