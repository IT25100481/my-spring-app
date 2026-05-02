package com.example.my_spring_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String FILE_PATH = "users.txt";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    // In-memory storage for now
    private final List<User> users = new ArrayList<>();

    public synchronized User saveUser(User user) {
        List<User> users = readUsers();

        if (user.getId() == null) {
            user.setId(generateNextId(users));
        } else {
            users.removeIf(existing -> existing.getId() != null && existing.getId().equals(user.getId()));
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now().toString());
        }
        user.setUpdatedAt(LocalDateTime.now().toString());

        users.add(user);
        writeUsers(users);
        return user;
    }

    public synchronized Optional<User> findByEmail(String email) {
        return readUsers().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public synchronized Optional<User> findByEmailAndIsActiveTrue(String email) {
        return readUsers().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(email))
                .filter(user -> Boolean.TRUE.equals(user.getIsActive()))
                .findFirst();
    }

    public synchronized Optional<User> findById(Long id) {
        return readUsers().stream()
                .filter(user -> user.getId() != null && user.getId().equals(id))
                .findFirst();
    }

    public synchronized boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }

    public synchronized List<User> getAllUsers() {
        return new ArrayList<>(readUsers());
    }

    private List<User> readUsers() {
        // Return in-memory list for now
        return new ArrayList<>(users);
    }

    private void writeUsers(List<User> usersList) {
        // Update in-memory list
        users.clear();
        users.addAll(usersList);
    }

    private User fromJson(String line) {
        try {
            User user = objectMapper.readValue(line, User.class);
            return user;
        } catch (IOException e) {
            System.err.println("DEBUG: Failed to parse user record: " + line);
            throw new RuntimeException("Unable to parse user record", e);
        }
    }

    private String toJson(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize user record", e);
        }
    }

    private Long generateNextId(List<User> users) {
        return users.stream()
                .map(User::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}
