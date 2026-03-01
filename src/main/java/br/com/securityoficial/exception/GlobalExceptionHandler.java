package br.com.securityoficial.exception;

import br.com.securityoficial.dto.response.ErrorResponse;
import br.com.securityoficial.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        var errorCode = ex.getErrorCode();

        var response = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                errorCode.getHttpStatus().value()
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String combinedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var response = new ErrorResponse(
                "VALIDATION_001",
                "Validation Error",
                combinedErrors,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        var errorCode = ErrorCode.ACCESS_DENIED;

        var response = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                errorCode.getHttpStatus().value()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        var errorCode = ErrorCode.INVALID_CREDENTIALS;

        var response = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                errorCode.getHttpStatus().value()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        var response = new ErrorResponse(
                "INTERNAL_001",
                "Internal Server Error",
                "An unexpected error occurred",
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.internalServerError().body(response);
    }
}