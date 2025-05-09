package com.example.HealthCare.dto.request.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateNoteRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;

    @NotNull(message = "Create date is required")
    @PastOrPresent(message = "Create date must be today or in the past")
    private java.time.LocalDate createAt;

    private Long noteIndex;
}