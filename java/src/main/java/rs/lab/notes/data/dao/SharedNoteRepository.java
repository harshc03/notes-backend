package rs.lab.notes.data.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.SharedNote;
import rs.lab.notes.data.model.SharedNoteAccessEnum;

@Repository
public interface SharedNoteRepository extends CrudRepository<SharedNote, Long>, PagingAndSortingRepository<SharedNote, Long>, JpaSpecificationExecutor<SharedNote> {

    Optional<SharedNote> findByIdAndOwnerId(Long id, Long ownerId);

    Page<SharedNote> findAllByOwnerId(Pageable page, Long ownerId);

    Optional<SharedNote> findByIdAndUserId(Long noteId, Long userId);

    Optional<SharedNote> findByIdAndUserIdAndAccess(Long id, Long userId, SharedNoteAccessEnum access);

    Page<SharedNote> findAllByUserId(Pageable pageable, Long userId);

    Page<SharedNote> getAllUserByNoteId(Long id, Pageable pageable);

    @Query("SELECT sn FROM SharedNote sn JOIN Note n ON sn.note.Id=n.Id WHERE sn.user.Id=:userId AND LOWER(n.caption) LIKE CONCAT('%', LOWER(:caption), '%')")
    Page<SharedNote> findAllByUserAndCaptionLike(Pageable pageable, @Param("userId") Long userId, @Param("caption") String caption);
}
