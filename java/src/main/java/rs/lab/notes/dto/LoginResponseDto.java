package rs.lab.notes.dto;

import java.util.Set;
import lombok.*;

@Data
@Builder
public class LoginResponseDto {

    private String fullName;
    private String email;
    private String[] roles;
    
    private String token;
    private long expiresIn;
}
