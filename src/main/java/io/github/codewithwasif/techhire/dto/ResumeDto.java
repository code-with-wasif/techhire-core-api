package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;



public final class ResumeDto {
    ResumeDto(){
    }

    public record CreateResumeRequest(
            @NotBlank(message = "User's full name is required") String fullName,
            @NotBlank(message = "Professional title is required") String professionalTitle,
            @NotBlank(message = "Skills are required") String skills,
            @NotBlank(message = "Portfolio URL is required")
            @URL(message = "Portfolio URL must be valid") String portfolioUrl,
            @NotBlank(message = "Bio is required") String bio
    ) {}

    @Builder
    public record ResumeResponse(
            Long id,
            String fullName,
            String professionalTitle,
            String skills,
            String portfolioUrl,
            String bio
    ) {}

    @Builder
    public record EmployerResumeResponse(
            @NotBlank String fullName,
            @NotBlank String professionalTitle,
            @NotBlank String skills,
            @NotBlank String portfolioUrl,
            @NotBlank String bio
    ) {}

}
