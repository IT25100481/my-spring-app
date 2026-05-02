package com.example.my_spring_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VendorService {

    private static final String FILE_PATH = "vendors.txt";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private UserService userService;

    public synchronized Vendor saveVendor(Vendor vendor) {
        List<Vendor> vendors = readVendors();

        if (vendor.getUser() != null) {
            userService.saveUser(vendor.getUser());
        }

        if (vendor.getId() == null) {
            vendor.setId(generateNextId(vendors));
        } else {
            vendors.removeIf(existing -> existing.getId() != null && existing.getId().equals(vendor.getId()));
        }

        if (vendor.getCreatedAt() == null) {
            vendor.setCreatedAt(LocalDateTime.now());
        }
        vendor.setUpdatedAt(LocalDateTime.now());

        vendors.add(vendor);
        writeVendors(vendors);
        return vendor;
    }

    public synchronized Optional<Vendor> findByUserEmail(String email) {
        return readVendors().stream()
                .filter(vendor -> vendor.getUser() != null && vendor.getUser().getEmail() != null)
                .filter(vendor -> vendor.getUser().getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public synchronized Optional<Vendor> findById(Long id) {
        return readVendors().stream()
                .filter(vendor -> vendor.getId() != null && vendor.getId().equals(id))
                .findFirst();
    }

    public synchronized boolean emailExists(String email) {
        return findByUserEmail(email).isPresent();
    }

    public synchronized List<Vendor> getAllVendors() {
        return new ArrayList<>(readVendors());
    }

    private List<Vendor> readVendors() {
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
            throw new RuntimeException("Unable to read vendors from file", e);
        }
    }

    private void writeVendors(List<Vendor> vendors) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            List<String> lines = vendors.stream()
                    .map(this::toJson)
                    .collect(Collectors.toList());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save vendors to file", e);
        }
    }

    private Vendor fromJson(String line) {
        try {
            return objectMapper.readValue(line, Vendor.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse vendor record", e);
        }
    }

    private String toJson(Vendor vendor) {
        try {
            return objectMapper.writeValueAsString(vendor);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize vendor record", e);
        }
    }

    private Long generateNextId(List<Vendor> vendors) {
        return vendors.stream()
                .map(Vendor::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}
