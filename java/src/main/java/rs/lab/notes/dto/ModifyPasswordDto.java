package rs.lab.notes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModifyPasswordDto {

    private String securityToken;
    @NotBlank
    private String oldPassword;
    @NotBlank
    //@ValidPassword
    private String newPassword;
}
