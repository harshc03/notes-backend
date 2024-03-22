package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import rs.lab.notes.data.model.*;
import java.util.Date;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsViewDto {

    private long id;
    private String fullName;
    private String email;
    private boolean locked;
    private Date createdAt;
    private Date updatedAt;
    private Set<Role> roles;
    private long numberOfNotes;
    private long numberOfShared;
}
