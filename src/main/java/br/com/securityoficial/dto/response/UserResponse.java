package br.com.securityoficial.dto.response;

public record UserResponse(
        Long id,
        String name,
        String username,
        String email){
}
