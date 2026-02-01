package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManageManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @GetMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.getAll();
    }


    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id).orElse(null);
        if (manufacturer == null) return ResponseEntity.notFound().build();
        manufacturer.setActive(false);
        manufacturerRepository.save(manufacturer);
        return ResponseEntity.ok("Manufacturer deactivated");
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activate(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id).orElse(null);
        if (manufacturer == null) return ResponseEntity.notFound().build();
        manufacturer.setActive(true);
        manufacturerRepository.save(manufacturer);
        return ResponseEntity.ok("Manufacturer activated");
    }

    @GetMapping("/active")
    public List<Manufacturer> getActiveManufacturers() {
        return manufacturerRepository.findByActiveTrue();
    }

    @GetMapping("/inactive")
    public List<Manufacturer> getInactiveManufacturers() {
        return manufacturerRepository.findByActiveFalse();
    }



    @PostMapping
    public ResponseEntity<Manufacturer> addManufacturer(@RequestBody Manufacturer manufacturer) {
        return ResponseEntity.ok(manufacturerService.save(manufacturer));
    }

    @GetMapping("/{id}")
    public Manufacturer getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable Long id, @RequestBody Manufacturer manufacturerDetails) {
        return ResponseEntity.ok(manufacturerService.update(id, manufacturerDetails));
    }


}
