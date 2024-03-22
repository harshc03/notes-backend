package rs.lab.notes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModifyProfileDto {

    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
}
