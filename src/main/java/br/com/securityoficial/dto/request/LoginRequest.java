package br.com.securityoficial.dto.request;

import br.com.securityoficial.infra.validation.AtLeastOneLoginMethod;
import jakarta.validation.constraints.NotBlank;

@AtLeastOneLoginMethod
public record LoginRequest(
        String email,
        String username,
        @NotBlank(message = "Password is required")
        String password){
}