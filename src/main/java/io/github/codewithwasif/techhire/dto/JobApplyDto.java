package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplyDto {
    @NotBlank
    private String coverLetterMessage;
    @NotNull(message = "Resume ID is required")
    private Long resumeId;

        @Builder
        public record EmployerResponse(String coverLetterMessage) {}
    }

