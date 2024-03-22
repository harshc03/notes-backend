package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import rs.lab.notes.data.model.User;
import rs.lab.notes.data.mapping.UserMapper;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.dto.LoginDto;
import rs.lab.notes.dto.LoginResponseDto;
import rs.lab.notes.dto.ModifyPasswordDto;
import rs.lab.notes.dto.ModifyProfileDto;
import rs.lab.notes.dto.RegisterDto;
import rs.lab.notes.services.AuthService;
import rs.lab.notes.services.AuthenticationFacade;
import rs.lab.notes.services.JwtService;
import rs.lab.notes.services.UserService;

@RestController
@Validated
@Tag(name = "Account")
public class AccountController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PostMapping(value = "/account.login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto login) {
        var authenticatedUser = authService.authenticate(login.getEmail(), login.getPassword());
        var jwtToken = jwtService.generateToken(authenticatedUser);
        var loginResponse = LoginResponseDto.builder()
                .fullName(authenticatedUser.getFullName())
                .email(authenticatedUser.getEmail())
                .roles(authenticatedUser.getRoles().stream().map(r -> r.getName().toString()).toArray(String[]::new))
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(value = "/account.logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/account.register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto register) {
        var authenticatedUser = userService.createUser(
                register.getFullName(),
                register.getEmail(),
                register.getPassword(),
                Set.of(RoleEnum.USER)
        );
        return ResponseEntity.ok(userMapper.toUserDto(authenticatedUser));
    }

    @GetMapping("/account.profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile() {
        var authentication = authenticationFacade.getAuthentication();
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @PutMapping("/account.profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modifyProfile(@Valid @RequestBody ModifyProfileDto update) {
        var authentication = authenticationFacade.getAuthentication();
        var user = (User) authentication.getPrincipal();

        var returnValue = userService.updateProfile(
                user.getId(),
                update.getFullName(),
                update.getEmail()
        ).orElseThrow();
        return ResponseEntity.ok(userMapper.toUserDto(returnValue));
    }

    @PostMapping("/account.modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modifyPassword(@Valid @RequestBody ModifyPasswordDto update) {
        var authentication = authenticationFacade.getAuthentication();
        var user = (User) authentication.getPrincipal();

        userService.modifyPassword(
                user.getId(), 
                update.getOldPassword(), 
                null, 
                update.getNewPassword()
        );
        return ResponseEntity.ok().build();

    }

    @PostMapping("/account.passwordReset")
    public ResponseEntity<?> passwordReset(@Valid @RequestBody ModifyPasswordDto update) {
        userService.modifyPassword(
                null, 
                null, 
                update.getSecurityToken(), 
                update.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}
