package rs.lab.notes.data.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.lab.notes.data.model.Role;
import rs.lab.notes.data.model.RoleEnum;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, PagingAndSortingRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);

    Optional<Set<Role>> findByNameIn(Set<RoleEnum> roles);

    @Query(value = "select r.* from roles r join users_roles ur on ur.roles_id=r.id where ur.user_id=:userId", nativeQuery = true)
    Set<Role> findUserRoles(Long userId);
}
