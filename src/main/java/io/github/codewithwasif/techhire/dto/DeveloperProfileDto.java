package io.github.codewithwasif.techhire.dto;

import io.github.codewithwasif.techhire.enums.WorkTypes;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public final class DeveloperProfileDto {
    private DeveloperProfileDto() {
    }

    public record CreateDeveloperProfileRequest(
            @NotBlank(message = "Full name is required")
            @Size(max = 100, message = "Full name cannot exceed 100 characters")
            String fullName,

            @NotBlank(message = "Headline is required")
            @Size(max = 150, message = "Headline cannot exceed 150 characters")
            String headline,

            @NotEmpty(message = "At least one skill is required")
            List<@NotBlank(message = "Skill entries cannot be blank") String> skills,

            @NotNull(message = "Work preference is required")
            WorkTypes workPreferences,

            @NotNull(message = "Years of experience is required")
            @PositiveOrZero(message = "Years of experience cannot be negative")
            Float yearsOfExperience,

            @NotBlank(message = "Summary is required")
            @Size(min = 20, max = 2000, message = "Summary must be between 20 and 2000 characters")
            String summary,

            @NotBlank(message = "Location is required")
            String location
    ) {
    }

    @Builder
    public record DeveloperProfileResponse(
            Long id,
            String fullName,
            String headline,
            List<String> skills,
            WorkTypes workPreferences,
            float yearsOfExperience,
            String summary,
            String location,
            LocalDateTime createdAt
    ) {
    }
}
