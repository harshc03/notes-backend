package rs.lab.notes.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.lab.notes.data.model.Note;

import java.util.Optional;
import rs.lab.notes.data.dao.PublicNoteRepository;
import rs.lab.notes.data.model.User;
import rs.lab.notes.services.AuthenticationFacade;
import rs.lab.notes.services.PublicNoteService;

@Service
@Transactional
public class PublicNoteServiceImpl implements PublicNoteService {

    @Autowired
    private PublicNoteRepository noteRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Optional<Note> getPublicNote(Long id) {
        var authUser = authenticationFacade.getAuthentication().getPrincipal();
        // Here could be authenticated use or "anonymus" user
        if (authUser instanceof User user) {
            return noteRepository.findPublicByIdOrUserId(id, user.getId());
        } else {
            return noteRepository.findPublicById(id);
        }
    }

    @Override
    public Page<Note> listPublicNotes(Pageable pageable) {
        var authUser = authenticationFacade.getAuthentication().getPrincipal();
        // Here could be authenticated use or "anonymus" user
        if (authUser instanceof User user) {
            return noteRepository.findAllByPublicOrUserId(user.getId(), pageable);
        } else {
            return noteRepository.findPublicAll(pageable);
        }
    }
}
