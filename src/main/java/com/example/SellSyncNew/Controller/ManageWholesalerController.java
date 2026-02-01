package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import com.example.SellSyncNew.Service.WholesalerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wholesaler")
public class ManageWholesalerController {

        @Autowired
        private WholesalerService wholesalerService;
        @Autowired
        private WholesalerRepository wholesalerRepository;

    @GetMapping
    public List<Wholesaler> getAllWholesalers() {
        return wholesalerService.getAllWholesalers(); // should return a List
    }


    @GetMapping("/active")
    public ResponseEntity<List<Wholesaler>> getActiveWholesalers() {
        List<Wholesaler> list = wholesalerRepository.findByActiveTrue();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Wholesaler>> getInactiveWholesalers() {
        List<Wholesaler> list = wholesalerRepository.findByActiveFalse();
        return ResponseEntity.ok(list);
    }

    // Already existing activate endpoint
    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateWholesaler(@PathVariable Long id) {
        Optional<Wholesaler> optional = wholesalerRepository.findById(id);
        if (optional.isPresent()) {
            Wholesaler wholesaler = optional.get();
            wholesaler.setActive(true);
            wholesalerRepository.save(wholesaler);
            return ResponseEntity.ok("Activated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wholesaler not found");
    }

    // Deactivate endpoint (already present)
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateWholesaler(@PathVariable Long id) {
        Optional<Wholesaler> optional = wholesalerRepository.findById(id);
        if (optional.isPresent()) {
            Wholesaler wholesaler = optional.get();
            wholesaler.setActive(false);
            wholesalerRepository.save(wholesaler);
            return ResponseEntity.ok("Deactivated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wholesaler not found");
    }


    @GetMapping("/{id}")
        public Wholesaler getById(@PathVariable Long id) {
            return wholesalerService.getWholesalerById(id);
        }

        @PostMapping
        public Wholesaler addWholesaler(@RequestBody Wholesaler wholesaler) {
            return wholesalerService.saveWholesaler(wholesaler);
        }

        @PutMapping("/{id}")
        public Wholesaler updateWholesaler(@PathVariable Long id, @RequestBody Wholesaler wholesaler) {
            wholesaler.setId(id);
            return wholesalerService.saveWholesaler(wholesaler);
        }

    }
