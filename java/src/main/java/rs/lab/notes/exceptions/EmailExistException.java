package rs.lab.notes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class EmailExistException extends AppException {

    private final String emailField;

    public EmailExistException(String emailField) {
        this.emailField = emailField;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        var returnValue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        returnValue.setProperty(emailField, "already registered");
        return returnValue;
    }
}
