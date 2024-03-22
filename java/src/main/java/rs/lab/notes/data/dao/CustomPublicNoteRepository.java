package rs.lab.notes.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.Note;

public interface CustomPublicNoteRepository {
    Page<Note> findAllByPublicOrUserId(Long userId, Pageable pageable);    
}
