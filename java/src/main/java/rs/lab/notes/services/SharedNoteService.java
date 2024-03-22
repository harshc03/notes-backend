package rs.lab.notes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import rs.lab.notes.data.model.SharedNote;
import rs.lab.notes.data.model.SharedNoteAccessEnum;

public interface SharedNoteService {

    Optional<SharedNote> createSharedNote(Long noteId, String userEmail, SharedNoteAccessEnum access);

    Optional<SharedNote> getSharedNote(Long noteId);

    Page<SharedNote> listSharedByMe(Pageable page);

    void deleteShare(Long id);

    Optional<SharedNote> modifyShareAccess(Long id, SharedNoteAccessEnum newAccess);

    Page<SharedNote> listUserShares(Pageable pageable);

    Optional<SharedNote> modifySharedNote(Long id, String body);

    Optional<SharedNote> getSharedNoteNote(Long id);

    Page<SharedNote> getAllUserByNoteId(Long id, Pageable pageable);

    Page<SharedNote> searchByNoteCaption(String query, Pageable pageable);
}
