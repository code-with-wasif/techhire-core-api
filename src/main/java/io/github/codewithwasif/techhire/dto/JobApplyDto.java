package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;


public final class JobApplyDto {
    private JobApplyDto() {
    }

    public record CreateApplicationRequest(
            @NotBlank(message = "Cover letter message is required") String coverLetterMessage,
            @NotNull(message = "Resume ID is required") Long resumeId
    ) {}

    @Builder
    public record ApplicationResponse(
            Long id,
            Long jobId,
            String coverLetterMessage,
            String applicationStatus,
            Long resumeId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}


        @Builder
        public record EmployerResponse(String coverLetterMessage) {}
    }