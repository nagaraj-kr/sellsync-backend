package com.example.SellSyncNew.Service;

import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public Optional<Manufacturer> findByEmail(String email) {
    return manufacturerRepository.findByEmail(email);
    }


    public List<Manufacturer> getAll() {
        return manufacturerRepository.findAll();
    }

    public Manufacturer getById(Long id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    public Manufacturer save(Manufacturer manufacturer) {
        manufacturer.setPassword(encoder.encode(manufacturer.getPassword()));
        return manufacturerRepository.save(manufacturer);
    }

    public Manufacturer update(Long id, Manufacturer manufacturerDetails) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manufacturer not found"));

        manufacturer.setEmail(manufacturerDetails.getEmail());
        manufacturer.setCompanyName(manufacturerDetails.getOrganizationName());
        manufacturer.setAddress(manufacturerDetails.getAddress());
        manufacturer.setGstNumber(manufacturerDetails.getGstNumber());
        manufacturer.setPassword(encoder.encode(manufacturerDetails.getPassword()));
        manufacturer.setPhone(manufacturerDetails.getPhone());
        return manufacturerRepository.save(manufacturer);
    }

    public void delete(Long id) {
        if (!manufacturerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manufacturer not found with ID: " + id);
        }
        manufacturerRepository.deleteById(id);
    }
    public void deactivateManufacturer(Long id) {
        Manufacturer m = manufacturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
        m.setActive(false);
        manufacturerRepository.save(m);
    }

}
