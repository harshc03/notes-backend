package rs.lab.notes.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.Note;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long>, PagingAndSortingRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    Optional<Note> findByCaption(String caption);

    Optional<Note> findByIdAndOwnerId(Long id, Long ownerId);

    Page<Note> findAllByOwnerId(Long ownerId, Pageable pageable);

    Optional<Note> findByCaptionAndOwnerId(String caption, Long ownerId);

    @Query(value = "SELECT u.email FROM User u JOIN SharedNote sn ON sn.user.id=u.id WHERE sn.note.id=:id")
    Set<String> FindAllUserShares(Long id);
}
