package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoteSharedDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String caption;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String state;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String body;

    @JsonProperty(value = "category", access = JsonProperty.Access.READ_ONLY)
    private String categoryName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String modifiedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String owner;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String access;
}
