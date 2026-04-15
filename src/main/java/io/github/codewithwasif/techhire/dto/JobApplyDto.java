package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplyDto {
    @NotBlank
    private String fullName;
    @NotBlank
    private String email;
    @NotBlank
    private String resumeLink;
    @NotBlank
    private String coverLetter;
    @NotBlank
    private String githubProfileUrl;
}
