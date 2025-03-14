package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.Document;
import com.example.HealthCare.repository.DocumentRepository;
import com.example.HealthCare.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j

public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document addDocument(Document document) {
        return this.documentRepository.save(document);
    }

    @Override
    public Document updateDocument(Document document) {
        Document check = this.documentRepository.findById(document.getDocumentID())
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        return this.documentRepository.save(document);
    }

    @Override
    public void deleteDocument(Integer documentID) {
        Document check = this.documentRepository.findById(documentID)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        this.documentRepository.deleteById(check.getDocumentID());
    }

    @Override
    public Page<Document> getAllDocuments(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return documentRepository.findByKeyword(keyword, pageable);
        }
        return documentRepository.findAll(pageable);
    }

    @Override
    public Optional<Document> findDocumentById(Integer documentID) {
        return this.documentRepository.findById(documentID);
    }
}
