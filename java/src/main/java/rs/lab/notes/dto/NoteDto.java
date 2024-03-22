package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.lab.notes.validation.NotBlankConditional;

@Getter
@Setter
@NoArgsConstructor
public class NoteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlankConditional
    private String caption;

    @NotEmpty
    @NotBlankConditional
    private String state;

    @NotEmpty
    private String body;

    @NotEmpty
    @JsonProperty("category")
    private String categoryName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String modifiedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String owner;
    
    private Boolean shared;
}
