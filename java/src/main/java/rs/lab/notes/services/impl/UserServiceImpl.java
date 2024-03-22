package rs.lab.notes.services.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import rs.lab.notes.data.dao.RoleRepository;
import rs.lab.notes.data.dao.UserRepository;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.data.model.User;
import rs.lab.notes.data.model.UserDetailsView;
import rs.lab.notes.exceptions.EmailExistException;
import rs.lab.notes.exceptions.InvalidActionException;
import rs.lab.notes.exceptions.ObjectNotExistException;
import rs.lab.notes.services.AuthenticationFacade;
import rs.lab.notes.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public User createUser(String fullName, String email, String password, Set<RoleEnum> roles) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistException("email");
        }

        var rolesList = roleRepository.findByNameIn(roles).orElseThrow();

        var passwordHash = passwordEncoder.encode(password);
        var entity = User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordHash)
                .locked(false)
                .roles(rolesList)
                .build();

        return userRepository.save(entity);
    }

    @Override
    public Optional<User> updateUser(Long id, String fullName, String email, Boolean locked, Set<RoleEnum> roles) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();

        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        var oldEmail = userRepository.findByEmail(email);
        if (oldEmail.isPresent() && !user.getId().equals(oldEmail.get().getId())) {
            throw new EmailExistException("email");
        }

        if (locked && authUser.getId().equals(user.getId())) {
            throw new InvalidActionException("You can't lock self");
        }

        var rolesList = roleRepository.findByNameIn(roles).orElseThrow();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setRoles(rolesList);
        user.setLocked(locked);
        userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    public Optional<User> updateProfile(Long id, String fullName, String email) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        var oldEmail = userRepository.findByEmail(email);
        if (oldEmail.isPresent() && !user.getId().equals(oldEmail.get().getId())) {
            throw new EmailExistException("email");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> listUsers(Pageable page) {
        return userRepository.findAll(page);
    }

    @Override
    public Page<UserDetailsView> listUsersDetailed(Pageable page) {
        return userRepository.findAllWithDetails(page);
    }
    
    @Override
    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ObjectNotExistException("id"));
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();

        if (user.getId().equals(authUser.getId())) {
            throw new InvalidActionException("You can't delete self");
        }
        userRepository.deleteById(user.getId());
    }

    @Override
    public Optional<User> setRoles(Long id, Set<RoleEnum> newRoles) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        if (user.getId().equals(authUser.getId())) {
            throw new InvalidActionException("You can't change role for self");
        }

        var roles = roleRepository.findByNameIn(newRoles).orElseThrow(() -> new IllegalArgumentException("roles"));
        user.setRoles(roles);
        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> modifyPassword(Long id, String oldPassword, String securityToken, String newPassword) {
        if (id == null) {
            if (StringUtils.hasLength(securityToken)) {
                // check security token
                throw new InvalidActionException("Invalid security token");
            }
        } else {
            var user = userRepository.findById(id).orElse(null);
            if (user != null && passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                var newPasswordHash = passwordEncoder.encode(newPassword);

                user.setPasswordHash(newPasswordHash);
                userRepository.save(user);

                return Optional.of(user);
            } else {
                throw new InvalidActionException("Old password does not match");
            }
        }

        return Optional.empty();
    }

    private static Specification<User> fullNameContains(String query) {
        return (user, cq, cb) -> cb.like(cb.lower(user.get("fullName")), cb.lower(cb.literal("%" + query + "%")));
    }

    private static Specification<User> emailContains(String query) {
        return (user, cq, cb) -> cb.like(cb.lower(user.get("email")), cb.lower(cb.literal("%" + query + "%")));
    }

    @Override
    public Page<User> searchByNameOrEmail(String query, Pageable pageable) {
        return this.userRepository.findAll(fullNameContains(query).or(emailContains(query)), pageable);
    }
}
