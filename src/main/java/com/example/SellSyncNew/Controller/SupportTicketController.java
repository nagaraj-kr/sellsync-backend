package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.Model.SupportTicket;
import com.example.SellSyncNew.Repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;
@RestController
@RequestMapping("/api/support")
public class SupportTicketController {

    @Autowired
    private SupportTicketRepository supportRepo;

    private final String UPLOAD_DIR = "uploads/support/";

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String username = authentication.getName(); // from UserDetails
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("role", role);

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitTicket(
            @RequestParam String username,
            @RequestParam String role,
            @RequestParam String subject,
            @RequestParam String orderId,
            @RequestParam String message,
            @RequestParam(required = false) MultipartFile image) {

        SupportTicket ticket = new SupportTicket();
        ticket.setUsername(username);
        ticket.setRole(role);
        ticket.setSubject(subject);
        ticket.setOrderId(orderId);
        ticket.setMessage(message);
        ticket.setStatus("Pending");

        // Save image if present
        if (image != null && !image.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, image.getBytes());
                ticket.setImagePath("/" + UPLOAD_DIR + filename);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Image upload failed");
            }
        }

        return ResponseEntity.ok(supportRepo.save(ticket));
    }

    // Admin: View all support tickets
    @GetMapping("/all")
    public List<SupportTicket> getAllTickets() {
        return supportRepo.findAll();
    }

    // Manufacturer / Wholesaler: Get tickets by username & role
    @GetMapping("/user")
    public List<SupportTicket> getUserTickets(@RequestParam String username, @RequestParam String role) {
        return supportRepo.findByUsernameIgnoreCaseAndRoleIgnoreCase(username, role);
    }

    // Admin: Update ticket status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<SupportTicket> opt = supportRepo.findById(id);
        if (opt.isPresent()) {
            SupportTicket ticket = opt.get();
            ticket.setStatus(status);
            return ResponseEntity.ok(supportRepo.save(ticket));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
