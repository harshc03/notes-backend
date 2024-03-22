package rs.lab.notes.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import rs.lab.notes.data.model.User;
import rs.lab.notes.data.dao.UserRepository;
import rs.lab.notes.exceptions.ObjectExistException;
import rs.lab.notes.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User authenticate(String email, String password) {
        var user = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return (User)user.getPrincipal();
    }
}
