package com.example.HealthCare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.HealthCare.exception.defineException.IdInvalidException;
import com.example.HealthCare.response.RestResponse;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

// ctrl + k + s lưu hết
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NoHandlerFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "No handler found for the requested URL");
        errorResponse.put("url", ex.getRequestURL());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            IdInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "You do not have permission to access this resource.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // @ExceptionHandler(MalformedJwtException.class)
    // public ResponseEntity<Map<String, String>>
    // handleMalformedJwtException(MalformedJwtException ex) {
    // Map<String, String> errorResponse = new HashMap<>();
    // errorResponse.put("error", "Invalid JWT format");
    // errorResponse.put("message", "The provided token is malformed or contains
    // illegal characters.");
    // return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    // }

}
