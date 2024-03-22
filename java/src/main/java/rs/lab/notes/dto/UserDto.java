package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.validation.NotBlankConditional;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlankConditional
//    @ValidPassword
    private String password;

    private Boolean locked;

    @NotNull
    @NotEmpty
    private Set<RoleEnum> roles;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long numberOfNotes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long numberOfShared;
}
