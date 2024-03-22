package rs.lab.notes.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.Note;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PublicNoteRepository extends CrudRepository<Note, Long>, PagingAndSortingRepository<Note, Long>, CustomPublicNoteRepository {

    @Query("SELECT n FROM Note n WHERE n.id=:id AND n.state=NoteStateEnum.PUBLIC")
    Optional<Note> findPublicById(@Param("id") Long id);

    @Query("SELECT n FROM Note n LEFT JOIN SharedNote sn ON sn.note.id=n.id WHERE (n.id=:id AND n.state=NoteStateEnum.PUBLIC) OR (n.id=:id AND sn.user.id=:userId) OR (n.id=:id AND n.owner.id=:userId)")
    Optional<Note> findPublicByIdOrUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM Note n WHERE n.state=NoteStateEnum.PUBLIC")
    Page<Note> findPublicAll(Pageable pageable);
}
