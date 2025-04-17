package com.agentathreya.conversationservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agentathreya.conversationservice.entity.UploadedFile;
import com.agentathreya.conversationservice.repository.UploadedFileRepository;
import com.agentathreya.conversationservice.utils.SecurityUtils;

@Service
public class FileUploadService {

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public UploadedFile storeFile(MultipartFile file) throws Exception {
        String username = SecurityUtils.getAuthenticatedUsername();

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFilename(file.getOriginalFilename());
        uploadedFile.setFileType(file.getContentType());
        uploadedFile.setData(file.getBytes());
        uploadedFile.setUploader(username);
        uploadedFile.setUploadTimestamp(LocalDateTime.now());

        return uploadedFileRepository.save(uploadedFile);
    }
}
