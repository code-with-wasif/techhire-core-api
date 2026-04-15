package io.github.codewithwasif.techhire.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Minimum salary is required")
    private Integer minSalary;

    @NotNull(message = "Maximum salary is required")
    private Integer maxSalary;

    @NotEmpty(message = "Tech stack must contain at least one skill")
    private List<String> techStack;

    @NotBlank(message = "Status cannot be empty")
    private String status;
}
