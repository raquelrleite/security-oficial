package br.com.securityoficial.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String username,
        String email){
}
