package com.example.HealthCare.controller;


import com.example.HealthCare.Util.SercurityUtil;
import com.example.HealthCare.model.Document;
import com.example.HealthCare.model.User;
import com.example.HealthCare.request.document.AddDocumentRequest;
import com.example.HealthCare.request.document.UpdateDocumentRequest;
import com.example.HealthCare.response.ApiResponse;
import com.example.HealthCare.service.DocumentService;
import com.example.HealthCare.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;

    public DocumentController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }


    @PostMapping("/documents")
    public ResponseEntity<Document> addDocument(@Valid @RequestBody AddDocumentRequest addDocumentRequest) {
        String email = SercurityUtil.getCurrentUserLogin().isPresent() ? SercurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        Document document = Document.builder()
                .recordID(addDocumentRequest.getRecordID())
                .fileName(addDocumentRequest.getFileName())
                .fileType(addDocumentRequest.getFileType())
                .fileContent(addDocumentRequest.getFileContent())
                .uploadDate(addDocumentRequest.getUploadDate())
                .build();
        log.info(document.toString());
        Document createddocument = this.documentService.addDocument(document);

        return new ResponseEntity<>(createddocument, HttpStatus.OK);
    }
    @PutMapping("/documents/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateDocumentRequest updateDocumentRequest) {
        Document document = Document.builder()
                .documentID(id)
                .recordID(updateDocumentRequest.getRecordID())
                .fileName(updateDocumentRequest.getFileName())
                .fileType(updateDocumentRequest.getFileType())
                .fileContent(updateDocumentRequest.getFileContent())
                .uploadDate(updateDocumentRequest.getUploadDate())
                .build();
        Document updatedDocument = documentService.updateDocument(document);

        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") Integer id) {
        this.documentService.deleteDocument(id);
        return ResponseEntity.ok().body("xóa thành công");
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Optional<Document>> getDocumentById(@PathVariable("id") Integer id) {
        Optional<Document> document = documentService.findDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SercurityUtil.getCurrentUserLogin().isPresent() ? SercurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);

        Page<Document> documentsPage = this.documentService.getAllDocuments(page,size,keyword,user.getId());
        List<Document> documents = documentsPage.getContent();

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

}
