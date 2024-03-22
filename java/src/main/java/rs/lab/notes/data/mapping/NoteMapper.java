package rs.lab.notes.data.mapping;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.dto.NoteDto;
import rs.lab.notes.dto.PageDto;

@Component
public class NoteMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public NoteMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Note.class, NoteDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getCategory().getName(), NoteDto::setCategoryName);
            mapper.map(src -> src.getOwner().getEmail(), NoteDto::setOwner);
        });
    }

    public NoteDto toNoteDto(Note note) {
        return modelMapper.map(note, NoteDto.class);
    }

    public PageDto<NoteDto> toPageDto(Page<Note> notes) {
        var pageDtoType = new TypeToken<PageDto<NoteDto>>() {
        }.getType();
        return modelMapper.map(notes, pageDtoType);
    }
}
