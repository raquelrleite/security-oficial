package br.com.securityoficial.exception;

import br.com.securityoficial.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String combinedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.joining(", ")); //

        var response = new ErrorResponse(
                "VALIDATION_001",
                "Validation Error",
                combinedErrors,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(response);
    }
}