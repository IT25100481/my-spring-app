package com.example.my_spring_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InquiryService {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // In-memory storage for inquiries
    private final List<Inquiry> inquiries = new ArrayList<>();

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
                .filter(inquiry -> "pending".equals(inquiry.getStatus()))
                .collect(ArrayList::new, (list, item) -> list.add(item), List::addAll);
    }

    private List<Inquiry> readInquiries() {
        // Return in-memory list
        return new ArrayList<>(inquiries);
    }

    private void writeInquiries(List<Inquiry> inquiriesList) {
        // Update in-memory list
        inquiries.clear();
        inquiries.addAll(inquiriesList);
    }

    private Long generateNextId(List<Inquiry> inquiries) {
        return inquiries.stream()
                .map(Inquiry::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}