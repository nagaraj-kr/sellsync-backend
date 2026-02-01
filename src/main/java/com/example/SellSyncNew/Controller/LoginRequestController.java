package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.AdminRepository;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class LoginRequestController {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired private ManufacturerRepository manufacturerRepo;
    @Autowired private WholesalerRepository wholesalerRepo;

    @GetMapping("/all")
    public Map<String, Object> getAllPendingUsers() {
        Map<String, Object> result = new HashMap<>();
        result.put("admins", adminRepo.findByStatus("PENDING"));
        result.put("manufacturers", manufacturerRepo.findByStatus("PENDING"));
        result.put("wholesalers", wholesalerRepo.findByStatus("PENDING"));
        return result;
    }

    @PostMapping("/approve/{type}/{id}")
    public String approveUser(@PathVariable String type, @PathVariable Long id) {
        if (type.equalsIgnoreCase("admin")) {
            Admin admin = adminRepo.findById(id).orElseThrow();
            admin.setStatus("APPROVED");
            adminRepo.save(admin);
        } else if (type.equalsIgnoreCase("manufacturer")) {
            Manufacturer m = manufacturerRepo.findById(id).orElseThrow();
            m.setStatus("APPROVED");
            manufacturerRepo.save(m);
        } else if (type.equalsIgnoreCase("wholesaler")) {
            Wholesaler w = wholesalerRepo.findById(id).orElseThrow();
            w.setStatus("APPROVED");
            wholesalerRepo.save(w);
        }
        return "Approved successfully!";
    }

    @DeleteMapping("/reject/{type}/{id}")
    public String rejectUser(@PathVariable String type, @PathVariable Long id) {
        if (type.equalsIgnoreCase("admin")) {
            adminRepo.deleteById(id);
        } else if (type.equalsIgnoreCase("manufacturer")) {
            manufacturerRepo.deleteById(id);
        } else if (type.equalsIgnoreCase("wholesaler")) {
            wholesalerRepo.deleteById(id);
        }
        return "Rejected and removed successfully!";
    }
}
