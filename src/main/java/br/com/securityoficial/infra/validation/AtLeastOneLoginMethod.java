package br.com.securityoficial.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // define onde uma anotação personalizada pode ser aplicada (para classes, métodos, campos, etc.)
@Retention(RetentionPolicy.RUNTIME) // define por quanto tempo a anotação personalizada estará disponível (em tempo de execução, em tempo de compilação, etc.)
@Constraint(validatedBy = LoginMethodValidator.class) // definir qual classe será responsável por validar a anotação personalizada
public @interface AtLeastOneLoginMethod {
    String message() default "Informe ao menos um método de identificação (Username ou E-mail)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}