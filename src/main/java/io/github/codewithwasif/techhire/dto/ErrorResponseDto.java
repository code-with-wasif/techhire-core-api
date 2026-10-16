package io.github.codewithwasif.techhire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponseDto(
            LocalDateTime localDateTime,
            int status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {

    }

