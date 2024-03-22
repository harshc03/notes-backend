package rs.lab.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)    
    private String modifiedAt;
}
