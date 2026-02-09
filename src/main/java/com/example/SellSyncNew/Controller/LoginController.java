package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.DTO.LoginRequest;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Service.AdminService;
import com.example.SellSyncNew.Service.ManufacturerService;
import com.example.SellSyncNew.Service.WholesalerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private WholesalerService wholesalerService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        System.out.println("Login start: " + loginRequest.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        // 🔍 check admin
        Optional<Admin> admin = adminService.findByEmail(loginRequest.getEmail());
        if (admin.isPresent()) {
            System.out.println("Admin login success");
            return ResponseEntity.ok(admin.get());
        }

        // 🔍 check manufacturer
        Optional<Manufacturer> manufacturer =
                manufacturerService.findByEmail(loginRequest.getEmail());
        if (manufacturer.isPresent()) {
            System.out.println("Manufacturer login success");
            return ResponseEntity.ok(manufacturer.get());
        }

        // 🔍 check wholesaler
        Optional<Wholesaler> wholesaler =
                wholesalerService.findByEmail(loginRequest.getEmail());
        if (wholesaler.isPresent()) {
            System.out.println("Wholesaler login success");
            return ResponseEntity.ok(wholesaler.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found");
    }
}
