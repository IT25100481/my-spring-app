package com.example.my_spring_app;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {

    private static final String FILE_PATH = "inquiries.txt";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public synchronized Inquiry saveInquiry(Inquiry inquiry) {
        List<Inquiry> inquiries = readInquiries();

        if (inquiry.getId() == null) {
            inquiry.setId(generateNextId(inquiries));
        } else {
            inquiries.removeIf(existing -> existing.getId() != null && existing.getId().equals(inquiry.getId()));
        }

        if (inquiry.getCreatedAt() == null) {
            inquiry.setCreatedAt(LocalDateTime.now().toString());
        }
        inquiry.setUpdatedAt(LocalDateTime.now().toString());

        inquiries.add(inquiry);
        writeInquiries(inquiries);
        return inquiry;
    }

    public synchronized Optional<Inquiry> findById(Long id) {
        return readInquiries().stream()
                .filter(inquiry -> inquiry.getId() != null && inquiry.getId().equals(id))
                .findFirst();
    }

    public synchronized List<Inquiry> getAllInquiries() {
        return new ArrayList<>(readInquiries());
    }

    public synchronized List<Inquiry> getPendingInquiries() {
        return readInquiries().stream()
                .filter(inquiry -> hasValue(inquiry.getStatus(), "pending"))
                .collect(Collectors.toList());
    }

    // Enhanced methods for admin management
    public synchronized List<Inquiry> getInquiriesByStatus(String status) {
        return readInquiries().stream()
                .filter(inquiry -> hasValue(inquiry.getStatus(), status))
                .collect(Collectors.toList());
    }

    public synchronized List<Inquiry> getInquiriesByType(String inquiryType) {
        return readInquiries().stream()
                .filter(inquiry -> hasValue(inquiry.getInquiryType(), inquiryType))
                .collect(Collectors.toList());
    }

    public synchronized List<Inquiry> getInquiriesByEmail(String email) {
        return readInquiries().stream()
                .filter(inquiry -> hasValue(inquiry.getEmail(), email))
                .collect(Collectors.toList());
    }

    public synchronized List<Inquiry> getInquiriesByPriority(String priority) {
        return readInquiries().stream()
                .filter(inquiry -> hasValue(inquiry.getPriority(), priority))
                .collect(Collectors.toList());
    }

    public synchronized List<Inquiry> searchInquiries(String searchTerm) {
        String lowerSearchTerm = searchTerm == null ? "" : searchTerm.trim().toLowerCase();
        if (lowerSearchTerm.isBlank()) {
            return getAllInquiries();
        }

        return readInquiries().stream()
                .filter(inquiry ->
                    (inquiry.getName() != null && inquiry.getName().toLowerCase().contains(lowerSearchTerm)) ||
                    (inquiry.getEmail() != null && inquiry.getEmail().toLowerCase().contains(lowerSearchTerm)) ||
                    (inquiry.getSubject() != null && inquiry.getSubject().toLowerCase().contains(lowerSearchTerm)) ||
                    (inquiry.getMessage() != null && inquiry.getMessage().toLowerCase().contains(lowerSearchTerm))
                )
                .collect(Collectors.toList());
    }

    public synchronized boolean updateInquiryStatus(Long id, String status, String adminNotes) {
        Optional<Inquiry> inquiryOpt = findById(id);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setStatus(status);
            if (adminNotes != null) {
                inquiry.setAdminNotes(adminNotes);
            }
            inquiry.setUpdatedAt(LocalDateTime.now().toString());
            saveInquiry(inquiry);
            return true;
        }
        return false;
    }

    public synchronized boolean assignInquiry(Long id, String assignedTo) {
        Optional<Inquiry> inquiryOpt = findById(id);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setAssignedTo(assignedTo);
            inquiry.setUpdatedAt(LocalDateTime.now().toString());
            saveInquiry(inquiry);
            return true;
        }
        return false;
    }

    public synchronized boolean respondToInquiry(Long id, String responseMessage) {
        Optional<Inquiry> inquiryOpt = findById(id);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setResponseMessage(responseMessage);
            inquiry.setStatus("responded");
            inquiry.setUpdatedAt(LocalDateTime.now().toString());
            saveInquiry(inquiry);
            return true;
        }
        return false;
    }

    public synchronized InquiryStatistics getInquiryStatistics() {
        List<Inquiry> allInquiries = readInquiries();
        return new InquiryStatistics(
            allInquiries.size(),
            (int) allInquiries.stream().filter(i -> hasValue(i.getStatus(), "pending")).count(),
            (int) allInquiries.stream().filter(i -> hasValue(i.getStatus(), "responded")).count(),
            (int) allInquiries.stream().filter(i -> hasValue(i.getStatus(), "completed")).count(),
            (int) allInquiries.stream().filter(i -> hasValue(i.getPriority(), "high") || hasValue(i.getPriority(), "urgent")).count()
        );
    }

    // Statistics class
    public static class InquiryStatistics {
        private int total;
        private int pending;
        private int responded;
        private int completed;
        private int highPriority;

        public InquiryStatistics(int total, int pending, int responded, int completed, int highPriority) {
            this.total = total;
            this.pending = pending;
            this.responded = responded;
            this.completed = completed;
            this.highPriority = highPriority;
        }

        // Getters
        public int getTotal() { return total; }
        public int getPending() { return pending; }
        public int getResponded() { return responded; }
        public int getCompleted() { return completed; }
        public int getHighPriority() { return highPriority; }
    }

    private List<Inquiry> readInquiries() {
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
            throw new RuntimeException("Unable to read inquiries from file", e);
        }
    }

    private void writeInquiries(List<Inquiry> inquiriesList) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = inquiriesList.stream()
                    .map(this::toJson)
                    .collect(Collectors.toList());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save inquiries to file", e);
        }
    }

    private Inquiry fromJson(String line) {
        try {
            return objectMapper.readValue(line, Inquiry.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse inquiry record", e);
        }
    }

    private String toJson(Inquiry inquiry) {
        try {
            return objectMapper.writeValueAsString(inquiry);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize inquiry record", e);
        }
    }

    private Long generateNextId(List<Inquiry> inquiries) {
        return inquiries.stream()
                .map(Inquiry::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    private boolean hasValue(String actual, String expected) {
        return actual != null && expected != null && actual.equalsIgnoreCase(expected);
    }
}
