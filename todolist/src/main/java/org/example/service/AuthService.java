package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    private UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false; // L'utilisateur existe déjà
        }

        String hashedPassword = hashPassword(password);
        User newUser = new User(username, hashedPassword);
        userRepository.save(newUser);
        return true;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            return user;
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
