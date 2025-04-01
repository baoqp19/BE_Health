package com.example.HealthCare.controller;

import com.example.HealthCare.dto.response.ApiResponse;
import com.example.HealthCare.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<String>> askQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String text = openAiService.askQuestion(question);
        String response = (text == null) ? "Error when calling the OpenAI API" : text;

        return ResponseEntity.ok(new ApiResponse<>(200, "Success", text));
    }

}
