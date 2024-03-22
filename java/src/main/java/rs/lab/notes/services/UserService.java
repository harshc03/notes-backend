package rs.lab.notes.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.lab.notes.data.model.User;

import java.util.Optional;
import java.util.Set;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.data.model.UserDetailsView;

public interface UserService {

    User createUser(String fullName, String email, String passwordHash, Set<RoleEnum> roles);

    Optional<User> updateUser(Long id, String fullName, String email, Boolean locked, Set<RoleEnum> roles);

    Optional<User> updateProfile(Long id, String fullName, String email);

    Optional<User> getUser(Long id);

    Optional<User> getUserByEmail(String email);

    Page<User> listUsers(Pageable page);

    /* Special endpoint to return list of users with additional details */
    public Page<UserDetailsView> listUsersDetailed(Pageable page);

    Page<User> searchByNameOrEmail(String query, Pageable pageable);

    void deleteUser(Long id);

    Optional<User> setRoles(Long id, Set<RoleEnum> newRoles);

    Optional<User> modifyPassword(Long id, String oldPassword, String securityToken, String newPassword);
}
