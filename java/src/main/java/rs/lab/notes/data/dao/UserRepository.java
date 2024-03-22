package rs.lab.notes.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User>, CustomUserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndLocked(String username, boolean locked);
}
