package rs.lab.notes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;

public class InvalidActionException extends AppException {

    private String message;

    public InvalidActionException() {
    }

    public InvalidActionException(String message) {
        this.message = message;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, StringUtils.hasLength(message) ? message : "Invalid action");
    }
}
