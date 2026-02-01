package com.example.SellSyncNew.Controller;


import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/admin")
    public String registerAdmin(@RequestBody Admin admin) {
        return registrationService.registerAdmin(admin);
    }

    @PostMapping("/manufacturer")
    public String registerManufacturer(@RequestBody Manufacturer manufacturer) {
        return registrationService.registerManufacturer(manufacturer);
    }

    @PostMapping("/wholesaler")
    public String registerWholesaler(@RequestBody Wholesaler wholesaler) {
        return registrationService.registerWholesaler(wholesaler);
    }
}
