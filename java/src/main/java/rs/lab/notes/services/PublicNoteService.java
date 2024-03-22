package rs.lab.notes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.Note;

import java.util.Optional;

public interface PublicNoteService {

    Optional<Note> getPublicNote(Long id);

    Page<Note> listPublicNotes(Pageable page);
}
