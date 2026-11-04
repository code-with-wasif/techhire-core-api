package io.github.codewithwasif.techhire.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EnhanceJobDescriptionRequest(

        @NotBlank(message = "Job title is required")
        @Size(max = 200)
        String title,

        @NotBlank(message = "Initial job description or rough notes are required")
        @Size(max = 8000)
        String description,

        List<String> techStack
) {}
