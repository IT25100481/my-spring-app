package com.example.my_spring_app;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class UserService {

    private static final String FILE_PATH = "users.txt";

    // Save user to users.txt
    public void saveUser(User user) throws IOException {
        FileWriter fw = new FileWriter(FILE_PATH, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(user.toString());
        bw.newLine();
        bw.close();
    }

    // Check if email already exists
    public boolean emailExists(String email) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 1 && parts[1].equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Get all users
    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return users;

        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                users.add(new User(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        return users;
    }
}