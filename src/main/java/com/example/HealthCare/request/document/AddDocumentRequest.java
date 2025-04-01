package com.example.HealthCare.request.document;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

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

//    @NotNull(message = "Upload date is required")
    @PastOrPresent(message = "Upload date must be a date in the past or present")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate uploadDate;

}
