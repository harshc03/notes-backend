package rs.lab.notes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class ObjectExistException extends AppException {

    private final String objectField;

    public ObjectExistException(String objectField) {
        this.objectField = objectField;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        var returnValue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        returnValue.setProperty(objectField, "already exist");
        return returnValue;
    }
}
