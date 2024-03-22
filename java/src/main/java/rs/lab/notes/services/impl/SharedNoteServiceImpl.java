package rs.lab.notes.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.lab.notes.data.model.User;
import rs.lab.notes.data.dao.NoteRepository;
import rs.lab.notes.services.AuthenticationFacade;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import rs.lab.notes.data.dao.SharedNoteRepository;
import rs.lab.notes.data.dao.UserRepository;
import rs.lab.notes.data.model.SharedNote;
import rs.lab.notes.data.model.SharedNoteAccessEnum;
import rs.lab.notes.exceptions.InvalidActionException;
import rs.lab.notes.exceptions.ObjectNotExistException;
import rs.lab.notes.services.SharedNoteService;

@Slf4j
@Service
@Transactional
public class SharedNoteServiceImpl implements SharedNoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SharedNoteRepository sharedNoteRepository;

    @Override
    public Optional<SharedNote> createSharedNote(Long noteId, String userEmail, SharedNoteAccessEnum access) {
        var user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        var note = noteRepository.findById(noteId).orElseThrow(() -> new ObjectNotExistException("note"));

        if (note.getOwner().getId().equals(user.getId())) {
            throw new InvalidActionException("You can't share with the owner");
        }

        if (sharedNoteRepository.findByIdAndUserId(noteId, user.getId()).isPresent()) {
            throw new InvalidActionException("The share already exist");
        }

        var newShare = SharedNote.builder()
                .note(note)
                .user(user)
                .owner(note.getOwner())
                .access(access)
                .build();

        var returnValue = sharedNoteRepository.save(newShare);
        return Optional.of(returnValue);
    }

    @Override
    public Page<SharedNote> listSharedByMe(Pageable page) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return sharedNoteRepository.findAllByOwnerId(page, authUser.getId());
    }

    @Override
    public void deleteShare(Long id) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var share = sharedNoteRepository.findByIdAndOwnerId(id, authUser.getId()).orElseThrow(() -> new ObjectNotExistException("share"));
        sharedNoteRepository.deleteById(share.getId());
    }

    @Override
    public Optional<SharedNote> getSharedNote(Long noteId) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return sharedNoteRepository.findByIdAndOwnerId(noteId, authUser.getId());
    }

    @Override
    public Optional<SharedNote> modifyShareAccess(Long id, SharedNoteAccessEnum newAccess) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var share = sharedNoteRepository.findByIdAndOwnerId(id, authUser.getId()).orElse(null);
        if (share == null) {
            return Optional.empty();
        }

        share.setAccess(newAccess);

        var returnValue = sharedNoteRepository.save(share);
        return Optional.of(returnValue);
    }

    @Override
    public Page<SharedNote> listUserShares(Pageable pageable) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return sharedNoteRepository.findAllByUserId(pageable, authUser.getId());
    }

    @Override
    public Optional<SharedNote> modifySharedNote(Long id, String body) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var share = sharedNoteRepository.findByIdAndUserIdAndAccess(id, authUser.getId(), SharedNoteAccessEnum.RW).orElse(null);
        if (share == null) {
            return Optional.empty();
        }

        var note = share.getNote();

        note.setBody(body);

        noteRepository.save(note);
        return Optional.of(share);
    }

    @Override
    public Optional<SharedNote> getSharedNoteNote(Long id) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        var share = sharedNoteRepository.findByIdAndUserId(id, authUser.getId());
        return share;
    }

    @Override
    public Page<SharedNote> getAllUserByNoteId(Long id, Pageable pageable) {
        return sharedNoteRepository.getAllUserByNoteId(id, pageable);
    }

    @Override
    public Page<SharedNote> searchByNoteCaption(String query, Pageable pageable) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        return this.sharedNoteRepository.findAllByUserAndCaptionLike(pageable, authUser.getId(), query);
    }
}
