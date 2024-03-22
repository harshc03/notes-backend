package rs.lab.notes.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.lab.notes.data.model.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
public class ChangeRoleDto {

    @NotEmpty
    private Set<RoleEnum> roles;
}
