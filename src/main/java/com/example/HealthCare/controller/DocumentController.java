package com.example.HealthCare.controller;


import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.model.Document;
import com.example.HealthCare.model.MedicalRecord;
import com.example.HealthCare.model.User;
import com.example.HealthCare.repository.MedicalRecordRepository;
import com.example.HealthCare.dto.request.document.AddDocumentRequest;
import com.example.HealthCare.dto.request.document.UpdateDocumentRequest;
import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.service.DocumentService;
import com.example.HealthCare.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;
    private final String BASE_DIRECTORY = "./src/main/resources/static/uploads/documents";
    private final String BASE_URL = "/documents/download";
    private final MedicalRecordRepository medicalRecordRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    public DocumentController(DocumentService documentService, UserService userService, MedicalRecordRepository medicalRecordRepository) {
        this.documentService = documentService;
        this.userService = userService;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    static class TestDto {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        public LocalDate uploadDate;
    }


    @GetMapping("documentss")
    public String getAll(){
      try{
          String  jsJon = "{\"uploadDate\": \"2024-03-30\"}";
          TestDto dto = objectMapper.readValue(jsJon, TestDto.class);
          System.out.println(dto.uploadDate); // Nếu ra kết quả, nghĩa là Jackson đã hoạt động
          log.info("uploadDate: " + dto.uploadDate);
      }catch (Exception e){
          e.printStackTrace();
      }
        return "thanhf coong";
    }

    @PostMapping("/documents")
    public ResponseEntity<?> addDocument(@Validated @RequestParam("file") MultipartFile file,
                                                 @RequestParam("request") @Valid String json) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);

        try {

            AddDocumentRequest addDocumentRequest = objectMapper.readValue(json, AddDocumentRequest.class);
            MedicalRecord medicalRecord = medicalRecordRepository.findById(addDocumentRequest.getId())
                    .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
            String subFolder = user.getEmail();
            Path directoryPath = Paths.get(BASE_DIRECTORY, subFolder);

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            String fileName = addDocumentRequest.getFileName();
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            Document document = Document.builder()
//                    .recordID(addDocumentRequest.getRecordID())
                    .fileName(addDocumentRequest.getFileName())
                    .fileType(addDocumentRequest.getFileType())
                    .uploadDate(addDocumentRequest.getUploadDate())
                    .build();
            log.info(document.toString());
            Document createddocument = this.documentService.addDocument(document);

            return new ResponseEntity<>(createddocument, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid request JSON format: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable("id") Integer id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("request") @Valid String json){
        try {
            String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

            User user = this.userService.handleGetUserByEmail(email);

            UpdateDocumentRequest updateDocumentRequest = objectMapper.readValue(json, UpdateDocumentRequest.class);
            MedicalRecord medicalRecord = this.medicalRecordRepository.findById(updateDocumentRequest.getRecordId())

                    .orElseThrow(() -> new RuntimeException("MedicalRecord not found"));
            String subFolder = user.getEmail();
            Path directoryPath = Paths.get(BASE_DIRECTORY, subFolder);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            String fileName = file.getOriginalFilename();
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            Document document = Document.builder()
                    .id(id)
//                    .recordID(updateDocumentRequest.getRecordID())
                    .fileName(updateDocumentRequest.getFileName())
                    .fileType(updateDocumentRequest.getFileType())
                    .uploadDate(updateDocumentRequest.getUploadDate())
                    .build();
            Document updatedDocument = documentService.updateDocument(document);

            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "File update failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") Integer id) {
        this.documentService.deleteDocument(id);
        return ResponseEntity.ok().body("xóa thành công");
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable("id") Integer id) {
        Optional<Document> documentOptional = documentService.findDocumentById(id);
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.handleGetUserByEmail(email);

        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            String subFolder = user.getEmail() + "/" + document.getFileName();
            String path = BASE_URL + subFolder;
            document.setPath(path);

            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Document not found", null));
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);

        Page<Document> documentsPage = this.documentService.getAllDocuments(page,size,keyword,user.getId());
        List<Document> documents = documentsPage.getContent();

        String subFolder = user.getEmail();
        for (Document document : documents) {
            document.setPath(BASE_URL + subFolder + "/" + document.getFileName());
        }


        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/download/{userFolder}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String userFolder,
            @PathVariable String fileName) throws IOException {
        System.out.println(userFolder + "/" + fileName);
        Path filePath = Paths.get(BASE_DIRECTORY, userFolder, fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            byte[] data = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(data);
        } else {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

}
