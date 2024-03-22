package rs.lab.notes.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NotBlankConditionalValidator.class)
@Documented
public @interface NotBlankConditional {

    String message() default "field must not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}