package rs.lab.notes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.data.model.NoteStateEnum;

import java.util.Optional;

public interface NoteService {

    Note createNote(String caption, NoteStateEnum state, String body, String category);

    Optional<Note> modifyNote(Long id, String caption, NoteStateEnum state, String body, String category);

    Optional<Note> getNote(Long id);

    Page<Note> listNotes(Pageable page);

    void deleteNote(Long id);

    Page<Note> searchByCaption(String query, Pageable pageable);
}
