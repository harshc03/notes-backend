package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.lab.notes.data.model.SharedNoteAccessEnum;
import rs.lab.notes.validation.NotBlankConditional;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SharedNoteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlankConditional
    @Email
    private String userEmail;

    @NotEmpty
    private String access = SharedNoteAccessEnum.RO.name();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modifiedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private NoteDto note;
}
