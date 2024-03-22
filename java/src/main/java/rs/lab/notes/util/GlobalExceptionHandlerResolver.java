package rs.lab.notes.util;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rs.lab.notes.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Component
@ControllerAdvice
public class GlobalExceptionHandlerResolver {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleBindMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var details = new ArrayList<String>();
        for (var e : ex.getFieldErrors()) {
            details.add(String.format("%s %s", e.getField(), e.getDefaultMessage()));
        }
        var errorResponse = new ErrorResponse("Validation failed", details);
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = AppException.class)
    protected ResponseEntity<?> handleBindAppException(AppException ex) {
        List<String> details = null;
        if (ex.getBody().getProperties() != null) {
            details = new ArrayList<>();
            for (var e : ex.getBody().getProperties().entrySet()) {
                details.add(String.format("%s %s", e.getKey(), e.getValue()));
            }
        }
        var errorResponse = new ErrorResponse(ex.getBody().getDetail(), details);
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    protected ResponseEntity<?> handleBindBadCredentialsException(BadCredentialsException ex) {
        var errorResponse = new ErrorResponse("Bad credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<?> handleBindUsernameNotFoundException(UsernameNotFoundException ex) {
        var errorResponse = new ErrorResponse("Invalid user");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = JwtException.class)
    protected ResponseEntity<?> handleBindJwtException(JwtException ex) {
        log.warn("{}: {}", ex.getClass().getName(), ex.getMessage());
        var errorResponse = new ErrorResponse("Token expired or invalid");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<?> handleBindIllegalArgumentException(IllegalArgumentException ex) {
        var errorResponse = new ErrorResponse("Illegal argument");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<?> handleBindAccessDeniedException(AccessDeniedException ex) {
        var errorResponse = new ErrorResponse("The operation you want to execute is forbidden for the user");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    protected ResponseEntity<?> handleBindException(PropertyReferenceException ex) {
        log.warn("{}: {}", ex.getClass().getName(), ex.getMessage());
        var errorResponse = new ErrorResponse("Referenced query field not found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    protected ResponseEntity<?> handleBindNoResourceFoundException(NoResourceFoundException ex) {
        var errorResponse = new ErrorResponse("No resource found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<?> handleBindException(HttpMessageNotReadableException ex) {
        log.warn("{}: {}", ex.getClass().getName(), ex.getMessage());
        var errorResponse = new ErrorResponse("Validation failed", List.of("Cannot deserialize value"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<?> handleBindException(Exception ex) {
        log.warn("{}: {}", ex.getClass().getName(), ex.getMessage());
        var errorResponse = new ErrorResponse("Something went wrong, please try again later");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
