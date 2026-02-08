package br.com.securityoficial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // USER ERRORS
    USER_NOT_FOUND("USER_001", "User not found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USER_002", "Email is already taken", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS("USER_003", "Username is already taken", HttpStatus.CONFLICT),

    // AUTHENTICATION ERRORS
    INVALID_CREDENTIALS("AUTH_001", "Invalid username or password", HttpStatus.UNAUTHORIZED),
    CREDENTIALS_NOT_FOUND("AUTH_002", "Authentication credentials were not found", HttpStatus.UNAUTHORIZED),
    TOKEN_GENERATION_ERROR("AUTH_003", "An error occurred while generating the access token", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_INVALID_OR_EXPIRED("AUTH_004", "The provided token is invalid or has expired", HttpStatus.UNAUTHORIZED);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
