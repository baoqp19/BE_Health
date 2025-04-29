package com.example.HealthCare.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    String uploadFile(MultipartFile file);

    String uploadFile(String path);

    Map<String, Object> deleteFile(String publicId) throws IOException;
}
