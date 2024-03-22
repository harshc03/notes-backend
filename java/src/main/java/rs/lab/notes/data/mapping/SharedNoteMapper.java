package rs.lab.notes.data.mapping;

import java.util.Set;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.data.model.SharedNote;
import rs.lab.notes.dto.PageDto;
import rs.lab.notes.dto.SharedNoteDto;
import org.modelmapper.Converter;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import rs.lab.notes.data.model.User;
import rs.lab.notes.dto.NoteDto;
import rs.lab.notes.dto.SharedUserDto;
import rs.lab.notes.dto.UserDto;

@Component
public class SharedNoteMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private UserMapper userMapper;

    public SharedNoteMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        {
            Converter<Note, NoteDto> noteConverter = c -> noteMapper.toNoteDto(c.getSource());
            var typeMap = modelMapper.emptyTypeMap(SharedNote.class, SharedNoteDto.class);
            typeMap.addMappings(mapper -> {
                mapper.map(SharedNote::getId, SharedNoteDto::setId);
                mapper.map(SharedNote::getCreatedAt, SharedNoteDto::setCreatedAt);
                mapper.map(SharedNote::getModifiedAt, SharedNoteDto::setModifiedAt);
                mapper.map(SharedNote::getAccess, SharedNoteDto::setAccess);
                mapper.map(src -> src.getUser().getEmail(), SharedNoteDto::setUser);
                mapper.using(noteConverter).map(SharedNote::getNote, SharedNoteDto::setNote);
            });
        }
        {
            Converter<User, UserDto> userConverter = c -> userMapper.toUserDto(c.getSource());
            var typeMap = modelMapper.emptyTypeMap(SharedNote.class, SharedUserDto.class);
            typeMap.addMappings(mapper -> {
                mapper.map(SharedNote::getId, SharedUserDto::setId);
                mapper.map(SharedNote::getCreatedAt, SharedUserDto::setCreatedAt);
                mapper.map(SharedNote::getModifiedAt, SharedUserDto::setModifiedAt);
                mapper.map(SharedNote::getAccess, SharedUserDto::setAccess);
                mapper.using(userConverter).map(SharedNote::getUser, SharedUserDto::setUser);
            });
        }
    }

    public SharedNoteDto toSharedNoteDto(SharedNote note) {
        return modelMapper.map(note, SharedNoteDto.class);
    }

    public Set<SharedNoteDto> toSetDto(Set<SharedNote> notes) {
        var setDtoType = new TypeToken<Set<SharedNoteDto>>() {
        }.getType();
        return modelMapper.map(notes, setDtoType);
    }

    public PageDto<SharedNoteDto> toPageDto(Page<SharedNote> notes) {
        var pageDtoType = new TypeToken<PageDto<SharedNoteDto>>() {
        }.getType();
        return modelMapper.map(notes, pageDtoType);
    }
    
    public PageDto<SharedUserDto> toPageSharedUserDto(Page<SharedNote> notes) {
        var pageDtoType = new TypeToken<PageDto<SharedUserDto>>() {
        }.getType();
        return modelMapper.map(notes, pageDtoType);
    }
}
