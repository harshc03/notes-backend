package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.lab.notes.data.mapping.UserMapper;
import rs.lab.notes.data.model.User;
import rs.lab.notes.dto.ChangeRoleDto;
import rs.lab.notes.dto.UserDto;
import rs.lab.notes.exceptions.InvalidActionException;
import rs.lab.notes.services.AuthenticationFacade;
import rs.lab.notes.services.UserService;

@RestController
@Validated
@Tag(name = "User")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users.get/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        var returnValue = userService.getUser(id);
        return (returnValue.isPresent())
                ? ResponseEntity.ok(userMapper.toUserDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users.create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto user) {
        var returnValue = userService.createUser(user.getFullName(), user.getEmail(), user.getPassword(), user.getRoles());
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserDto(returnValue));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users.modify/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto user) {
        var returnValue = userService.updateUser(id, user.getFullName(), user.getEmail(), user.getLocked(), user.getRoles());
        return (returnValue.isPresent())
                ? ResponseEntity.ok(userMapper.toUserDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users.list/detailed")
    public ResponseEntity<?> listUsersDetailed(@ParameterObject Pageable pageable) {
        var returnValue = userService.listUsersDetailed(pageable);
        return ResponseEntity.ok(userMapper.toPageDetailsViewDto(returnValue));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users.delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users.setRoles/{id}")
    public ResponseEntity<?> setRoles(@PathVariable("id") Long id, @Valid @RequestBody ChangeRoleDto role) {
        var authUser = (User) authenticationFacade.getAuthentication().getPrincipal();
        if (authUser.getId().equals(id)) {
            throw new InvalidActionException("Can't change the role for self");
        }

        var returnValue = userService.setRoles(id, role.getRoles());
        return (returnValue.isPresent())
                ? ResponseEntity.ok(userMapper.toUserDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users.list")
    public ResponseEntity<?> listUsers(@ParameterObject Pageable pageable) {
        var returnValue = userService.listUsers(pageable);
        return ResponseEntity.ok(userMapper.toPageDto(returnValue));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users.search")
    public ResponseEntity<?> searchUser(@RequestParam(value = "q", required = false) String query, @ParameterObject Pageable pageable) {
        Page<User> returnValue;
        if (StringUtils.hasLength(query)) {
            returnValue = userService.searchByNameOrEmail(query, pageable);
        } else {
            returnValue = userService.listUsers(pageable);
        }
        return ResponseEntity.ok(userMapper.toPageDto(returnValue));
    }
}
