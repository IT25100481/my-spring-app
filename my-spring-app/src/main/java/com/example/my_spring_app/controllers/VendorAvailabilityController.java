package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Availability;
import com.example.my_spring_app.models.AvailabilityStatus;
import com.example.my_spring_app.services.AvailabilityManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/availability")
@CrossOrigin(origins = "*")
public class VendorAvailabilityController {

    private final AvailabilityManagementService availabilityManagementService;

    public VendorAvailabilityController(AvailabilityManagementService availabilityManagementService) {
        this.availabilityManagementService = availabilityManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Availability>> getAvailability(@RequestParam String email) {
        return ResponseEntity.ok(availabilityManagementService.getAvailability(email));
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<Availability>> getCalendar(@RequestParam String email,
                                                          @RequestParam String startDate,
                                                          @RequestParam String endDate) {
        return ResponseEntity.ok(availabilityManagementService.getCalendar(email, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @PostMapping
    public ResponseEntity<?> setAvailability(@RequestParam String email,
                                             @RequestParam String date,
                                             @RequestParam AvailabilityStatus status,
                                             @RequestParam(required = false) String startTime,
                                             @RequestParam(required = false) String endTime,
                                             @RequestParam(required = false) String reason) {
        try {
            LocalTime start = startTime == null || startTime.isBlank() ? null : LocalTime.parse(startTime);
            LocalTime end = endTime == null || endTime.isBlank() ? null : LocalTime.parse(endTime);
            return ResponseEntity.ok(availabilityManagementService.setAvailability(email, LocalDate.parse(date), status, start, end, reason));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockDate(@RequestParam String email,
                                       @RequestParam String date,
                                       @RequestParam(required = false) String reason) {
        try {
            return ResponseEntity.ok(availabilityManagementService.blockDate(email, LocalDate.parse(date), reason));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockDate(@RequestParam String email,
                                         @RequestParam String date) {
        try {
            return ResponseEntity.ok(availabilityManagementService.unblockDate(email, LocalDate.parse(date)));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/working-hours")
    public ResponseEntity<?> setWorkingHours(@RequestParam String email,
                                             @RequestParam DayOfWeek dayOfWeek,
                                             @RequestParam String startTime,
                                             @RequestParam String endTime) {
        try {
            return ResponseEntity.ok(availabilityManagementService.setWorkingHours(email, dayOfWeek, LocalTime.parse(startTime), LocalTime.parse(endTime)));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
