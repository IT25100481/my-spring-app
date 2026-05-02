package com.example.my_spring_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String FILE_PATH = "users.txt";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public synchronized User saveUser(User user) {
        List<User> users = readUsers();

        if (user.getId() == null) {
            user.setId(generateNextId(users));
        } else {
            users.removeIf(existing -> existing.getId() != null && existing.getId().equals(user.getId()));
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());

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
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            return Files.lines(path)
                    .filter(line -> !line.isBlank())
                    .map(this::fromJson)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read users from file", e);
        }
    }

    private void writeUsers(List<User> users) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            List<String> lines = users.stream()
                    .map(this::toJson)
                    .collect(Collectors.toList());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save users to file", e);
        }
    }

    private User fromJson(String line) {
        try {
            return objectMapper.readValue(line, User.class);
        } catch (IOException e) {
            return fromLegacyCsv(line);
        }
    }

    private User fromLegacyCsv(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 4) {
            throw new RuntimeException("Unable to parse user record");
        }

        User user = new User();
        user.setFullName(parts[0].trim());
        user.setEmail(parts[1].trim());
        user.setPassword(parts[2].trim());
        user.setPhone(parts[3].trim());
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return user;
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
