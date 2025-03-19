package com.example.HealthCare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.key}")
    private String apiKey;

    public String askQuestion(String question) {
        String urlText = url + "?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{ \"contents\": [" +
                "{\"role\": \"user\", \"parts\": [{\"text\": \"" + question + "\"}]}" +
                "]}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                urlText,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully called the OpenAI API");
            String anwser = extractTextFromResponse(response.getBody());
            log.info(anwser);
            return anwser;
        } else {
            log.error("Failed to call the OpenAI API: {}", response.getStatusCode());
            return null;
        }
    }

    private String extractTextFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (IOException e) {
            log.error("Error parsing response body", e);
            return null;
        }
    }


}