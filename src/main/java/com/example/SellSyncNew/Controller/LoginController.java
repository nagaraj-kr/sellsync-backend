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
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

  @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );

    // try admin
    Optional<Admin> admin = adminService.findByEmail(loginRequest.getEmail());
    if(admin.isPresent()){
        return ResponseEntity.ok(admin.get());
    }

    // try manufacturer
    Optional<Manufacturer> manufacturer =
        manufacturerService.findByEmail(loginRequest.getEmail());
    if(manufacturer.isPresent()){
        return ResponseEntity.ok(manufacturer.get());
    }

    // try wholesaler
    Optional<Wholesaler> wholesaler =
        wholesalerService.findByEmail(loginRequest.getEmail());
    if(wholesaler.isPresent()){
        return ResponseEntity.ok(wholesaler.get());
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("User not found");
}

        
}
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
//             authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                     loginRequest.getEmail(),
//                     loginRequest.getPassword()
//                 )
//             );

//             Admin admin = adminService.getAdminByEmail(loginRequest.getEmail());

//             admin.setPassword(null); // safety
//             return ResponseEntity.ok(admin);

//         } catch (AuthenticationException e) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                     .body("Invalid username or password");
//         }
//     }
// }

