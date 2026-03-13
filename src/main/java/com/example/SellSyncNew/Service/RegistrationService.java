package com.example.SellSyncNew.Service;




import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired private ManufacturerRepository manufacturerRepo;
    @Autowired private WholesalerRepository wholesalerRepo;
    @Autowired private PasswordEncoder passwordEncoder;


    public String registerManufacturer(Manufacturer manufacturer) {
        manufacturer.setPassword(passwordEncoder.encode(manufacturer.getPassword()));
        manufacturer.setStatus("PENDING");
        manufacturerRepo.save(manufacturer);
        return "Manufacturer registration request submitted!";
    }

    public String registerWholesaler(Wholesaler wholesaler) {
        wholesaler.setPassword(passwordEncoder.encode(wholesaler.getPassword()));
        wholesaler.setStatus("APPROVED");
        wholesalerRepo.save(wholesaler);
        return "Wholesaler registration request submitted!";
    }
}
