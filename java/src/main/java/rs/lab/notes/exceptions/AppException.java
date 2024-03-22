package rs.lab.notes.exceptions;

import org.springframework.web.ErrorResponse;

public abstract class AppException extends RuntimeException implements ErrorResponse {
}
