// package com.example.SellSyncNew.Controller;

// import com.example.SellSyncNew.DTO.LoginRequest;
// import com.example.SellSyncNew.Model.Admin;
// import com.example.SellSyncNew.Model.Manufacturer;
// import com.example.SellSyncNew.Model.Wholesaler;
// import com.example.SellSyncNew.Service.AdminService;
// import com.example.SellSyncNew.Service.ManufacturerService;
// import com.example.SellSyncNew.Service.WholesalerService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.web.bind.annotation.*;
// import java.util.Optional;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.*;


// @RestController
// @RequestMapping("/api/auth")
// public class LoginController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private AdminService adminService;

//     @Autowired
//     private ManufacturerService manufacturerService;

//     @Autowired
//     private WholesalerService wholesalerService;


//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

//         System.out.println("Login start: " + loginRequest.getEmail());

//         try {
//             authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(
//                             loginRequest.getEmail(),
//                             loginRequest.getPassword()
//                     )
//             );
//         } catch (AuthenticationException e) {
//             System.out.println("Authentication failed");
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                     .body("Invalid credentials");
//         }

//         // 🔍 check admin
//         Optional<Admin> admin = adminService.findByEmail(loginRequest.getEmail());
//         if (admin.isPresent()) {
//             System.out.println("Admin login success");
//            Map<String,Object> response = new HashMap<>();
// response.put("role","ADMIN");
// response.put("data",admin.get());

// return ResponseEntity.ok(response);

//         }

//         // 🔍 check manufacturer
//         Optional<Manufacturer> manufacturer =
//                 manufacturerService.findByEmail(loginRequest.getEmail());
//         if (manufacturer.isPresent()) {
//             System.out.println("Manufacturer login success");
//            Map<String,Object> response = new HashMap<>();
// response.put("role","MANUFACTURER");
// response.put("data",manufacturer.get());

// return ResponseEntity.ok(response);

//         }

//         // 🔍 check wholesaler
//         Optional<Wholesaler> wholesaler =
//                 wholesalerService.findByEmail(loginRequest.getEmail());
//         if (wholesaler.isPresent()) {
//             System.out.println("Wholesaler login success");
//            Map<String,Object> response = new HashMap<>();
// response.put("role","WHOLESALER");
// response.put("data",wholesaler.get());

// return ResponseEntity.ok(response);

//         }

//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                 .body("User not found");
//     }
// }
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) {

        System.out.println("Login start: " + loginRequest.getEmail());

        Authentication authentication;

        try {

            authentication = authenticationManager.authenticate(
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

        // ⭐ SECURITY CONTEXT CREATE
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // ⭐ SESSION CREATE (IMPORTANT)
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        // 🔍 ADMIN CHECK
        Optional<Admin> admin = adminService.findByEmail(loginRequest.getEmail());

        if (admin.isPresent()) {

            System.out.println("Admin login success");

            Map<String, Object> response = new HashMap<>();
            response.put("role", "ADMIN");
            response.put("data", admin.get());

            return ResponseEntity.ok(response);
        }

        // 🔍 MANUFACTURER CHECK
        Optional<Manufacturer> manufacturer =
                manufacturerService.findByEmail(loginRequest.getEmail());

        if (manufacturer.isPresent()) {

            System.out.println("Manufacturer login success");

            Map<String, Object> response = new HashMap<>();
            response.put("role", "MANUFACTURER");
            response.put("data", manufacturer.get());

            return ResponseEntity.ok(response);
        }

        // 🔍 WHOLESALER CHECK
        Optional<Wholesaler> wholesaler =
                wholesalerService.findByEmail(loginRequest.getEmail());

        if (wholesaler.isPresent()) {

            System.out.println("Wholesaler login success");

            Map<String, Object> response = new HashMap<>();
            response.put("role", "WHOLESALER");
            response.put("data", wholesaler.get());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found");
    }
}
