package io.github.codewithwasif.techhire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplyDto {
    @NotBlank
    private String coverLetterMessage;
}
