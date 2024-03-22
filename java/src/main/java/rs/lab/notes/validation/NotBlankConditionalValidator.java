package rs.lab.notes.validation;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

public class NotBlankConditionalValidator implements ConstraintValidator<NotBlankConditional, String> {

    @Resource
    private HttpServletRequest request;

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return !("POST".equals(request.getMethod()) && !isBlank(value));
    }

    private boolean isBlank(final String value) {
        return StringUtils.hasLength(value);
    }
}