package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.util.List;


public final class JobPostDto {
    private JobPostDto() {
    }

    public record CreateJobPostRequest(

            @NotBlank(message = "Title cannot be empty") String title,
            @NotBlank(message = "Company name is required") String companyName,
            @NotBlank(message = "Description cannot be empty") String description,
            @NotNull(message = "Minimum salary is required") @Min(value = 0, message = "Salary cannot be negative") Integer minSalary,
            @NotNull(message = "Maximum salary is required") @Min(value = 0, message = "Salary cannot be negative") Integer maxSalary,
            @NotEmpty(message = "Tech stack must contain at least one skill") List<String> techStack,
            @NotBlank(message = "Status cannot be empty") String status
    ) {}

    public record UpdateJobPostRequest( String title, String companyName, String description,
                                 Integer minSalary, Integer maxSalary,
                                 List<String> techStack, String status
    ) {}

    @Builder
    public record JobPostResponse( Long id, String title, String companyName, String description,
                            Integer minSalary, Integer maxSalary,
                            List<String> techStack, String status
    ) {}

    public record ApplicantReviewResponse(
            JobApplyDto.EmployerResponse application,
            ResumeDto.EmployerResumeResponse resume
    ) {}
}
