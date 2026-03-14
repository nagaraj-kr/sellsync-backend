
package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.Config.CustomUserDetails;
import com.example.SellSyncNew.DTO.AdminDTO;
import com.example.SellSyncNew.DTO.AdminPasswordChangeDTO;
import com.example.SellSyncNew.DTO.AdminUpdateProfileDTO;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/settings")
public class AdminSettingController {

    @Autowired
    private AdminService adminService;

    // ✅ Get Admin Profile Info by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        try {
            Optional<Admin> optionalAdmin = adminService.getAdminById(id);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                AdminDTO adminDTO = new AdminDTO(
                        admin.getId(),
                        admin.getUsername(),
                        admin.getEmail(),
                        admin.getPhone()
                );
                return ResponseEntity.ok(adminDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Admin not found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching admin: " + e.getMessage());
        }
    }

    // ✅ Update Profile Info
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody AdminUpdateProfileDTO dto) {
        try {
            Admin updatedAdmin = adminService.updateProfile(dto);
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed: " + e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AdminPasswordChangeDTO dto, Principal principal) {
        try {
            String email = principal.getName(); // assuming admin logs in with email
            adminService.changePasswordByEmail(email, dto);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    // ✅ Get Currently Logged-in Admin
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        Object principal = authentication.getPrincipal();
       

        if (!(principal instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Principal is not CustomUserDetails: " + principal.getClass().getName());
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Admin admin = userDetails.getAdmin();

        AdminDTO adminDTO = new AdminDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getPhone()
        );

        return ResponseEntity.ok(adminDTO);
    }

}
