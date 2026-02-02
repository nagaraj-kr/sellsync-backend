package com.example.SellSyncNew.Controller;



import com.example.SellSyncNew.DTO.LoginRequest;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Repository.AdminRepository;
import com.example.SellSyncNew.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

// @RestController
// @RequestMapping("/api/auth")
// public class LoginController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private AdminService adminService;

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//         try {
//             Authentication auth = authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(
//                             loginRequest.getEmail(),
//                             loginRequest.getPassword()
//                     )
//             );

//             // Fetch the admin details from the database
//             Admin admin = adminService.getAdminByEmail(loginRequest.getEmail());

//             // Avoid sending password back to frontend
//             Admin safeAdmin = new Admin();
//             safeAdmin.setId(admin.getId());
//             safeAdmin.setUsername(admin.getUsername());
//             safeAdmin.setEmail(admin.getEmail());
//             safeAdmin.setPhone(admin.getPhone());

//             return ResponseEntity.ok(safeAdmin);

//         } catch (AuthenticationException e) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//         }
//     }
// }
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://sellsync-frontend.netlify.app", allowCredentials = "true")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            Admin admin = adminService.getAdminByEmail(loginRequest.getEmail());

            admin.setPassword(null); // safety
            return ResponseEntity.ok(admin);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}

