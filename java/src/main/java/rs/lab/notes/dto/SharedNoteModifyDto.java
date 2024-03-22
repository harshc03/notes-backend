package rs.lab.notes.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SharedNoteModifyDto {

    @NotEmpty
    private String body;
}
