package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;



public final class JobApplyDto {
    private JobApplyDto() {
    }

    public record CreateApplicationRequest(
            @NotBlank(message = "Cover letter message is required") String coverLetterMessage,
            @NotNull(message = "Resume ID is required") Long resumeId
    ) {}

        @Builder
        public record EmployerResponse(String coverLetterMessage) {}
    }