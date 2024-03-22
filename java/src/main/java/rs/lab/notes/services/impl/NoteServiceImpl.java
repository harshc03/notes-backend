package rs.lab.notes.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.data.model.NoteStateEnum;
import rs.lab.notes.data.model.User;
import rs.lab.notes.exceptions.ObjectExistException;
import rs.lab.notes.data.dao.CategoryRepository;
import rs.lab.notes.data.dao.NoteRepository;
import rs.lab.notes.services.AuthenticationFacade;
import rs.lab.notes.services.NoteService;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import rs.lab.notes.exceptions.ObjectNotExistException;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Note createNote(String caption, NoteStateEnum state, String body, String categoryName) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();

        if (noteRepository.findByCaptionAndOwnerId(caption, authUser.getId()).isPresent()) {
            throw new ObjectExistException("caption");
        }

        var category = categoryRepository.findByName(categoryName).orElseThrow(() -> new IllegalArgumentException());

        var entity = Note.builder()
                .caption(caption)
                .state(state)
                .body(body)
                .category(category)
                .owner(authUser)
                .build();

        return noteRepository.save(entity);
    }

    @Override
    public Optional<Note> modifyNote(Long id, String caption, NoteStateEnum state, String body, String categoryName) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var note = noteRepository.findByIdAndOwnerId(id, authUser.getId()).orElse(null);
        if (note == null) {
            return Optional.empty();
        }

        var old = noteRepository.findByCaptionAndOwnerId(caption, authUser.getId());
        if (old.isPresent() && !note.getId().equals(old.get().getId())) {
            throw new ObjectExistException("caption");
        }

        var category = categoryRepository.findByName(categoryName).orElseThrow(() -> new IllegalArgumentException());

        note.setCaption(caption);
        note.setState(state);
        note.setBody(body);
        note.setCategory(category);
        noteRepository.save(note);

        return Optional.of(note);
    }

    @Override
    public Optional<Note> getNote(Long id) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return noteRepository.findByIdAndOwnerId(id, authUser.getId());
    }

    @Override
    public Page<Note> listNotes(Pageable page) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return noteRepository.findAllByOwnerId(authUser.getId(), page);
    }

    @Override
    public void deleteNote(Long id) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var note = noteRepository.findByIdAndOwnerId(id, authUser.getId()).orElseThrow(() -> new ObjectNotExistException("note"));
        noteRepository.deleteById(note.getId());
    }

    private static Specification<Note> captionContains(String query) {
        return (note, cq, cb) -> cb.like(cb.lower(note.get("caption")), cb.lower(cb.literal("%" + query + "%")));
    }

    private static Specification<Note> ownerEquals(User owner) {
        return (note, cq, cb) -> cb.equal(note.get("owner"), owner);
    }
    
    @Override
    public Page<Note> searchByCaption(String query, Pageable pageable) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return this.noteRepository.findAll(captionContains(query).and(ownerEquals(authUser)), pageable);
    }
}
