package br.com.securityoficial.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String error,
        String message,
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss.SSSSSS")
        LocalDateTime timestamp,
        int status
) {
}
