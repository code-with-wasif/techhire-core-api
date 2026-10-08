package io.github.codewithwasif.techhire.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String fullName;
    @NotBlank
    private String professionalTitle;
    @NotBlank
    private String skills;
    @NotBlank
    private String portfolioUrl;
    @NotBlank
    private String bio;

}
