package rs.lab.notes.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
public class ErrorResponse {

    public ErrorResponse(String message) {
        this(message, null);
    }

    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    //General error message about nature of error
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //Specific errors in API request processing
    private List<String> details;
}
