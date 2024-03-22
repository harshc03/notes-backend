package rs.lab.notes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class PageDto<T> {

    private Collection<T> content;
    private long totalElements;
}
