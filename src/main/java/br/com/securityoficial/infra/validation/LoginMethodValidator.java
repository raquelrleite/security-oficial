package br.com.securityoficial.infra.validation;

import br.com.securityoficial.dto.request.LoginRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginMethodValidator implements ConstraintValidator<AtLeastOneLoginMethod, LoginRequest> {

    @Override
    public boolean isValid(LoginRequest value, ConstraintValidatorContext context) {
        if (value == null) return false;

        // Verifica se ao menos um dos campos de identificação tem texto
        boolean hasUsername = value.username() != null && !value.username().isBlank();
        boolean hasEmail = value.email() != null && !value.email().isBlank();

        return hasUsername || hasEmail;
    }
}
