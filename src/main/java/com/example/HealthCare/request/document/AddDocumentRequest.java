package com.example.HealthCare.request.document;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDocumentRequest {
    private Integer documentID;

    private Integer recordID;

    @NotBlank(message = "fileName type is required")
    @Size(max = 50, message = "File name type must not exceed 50 characters")
    private String fileName;

    @NotBlank(message = "File type is required")
    @Size(max = 20, message = "File type must not exceed 20 characters")
    private String fileType;

    @NotBlank(message = "File content is required")
    @Size(max = 500, message = "File content must not exceed 500 characters")
    private String fileContent;

    @NotNull(message = "Upload date is required")
    @Past(message = "Upload date must be a date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.time.LocalDate uploadDate;
}
