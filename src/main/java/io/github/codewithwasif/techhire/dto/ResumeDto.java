package io.github.codewithwasif.techhire.dto;

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
