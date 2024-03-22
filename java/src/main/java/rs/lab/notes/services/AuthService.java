package rs.lab.notes.services;

import rs.lab.notes.data.model.User;

public interface AuthService {

    User authenticate(String email, String password);
}
