package com.example.SellSyncNew.Service;


import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WholesalerService {

    @Autowired
    private WholesalerRepository wholesalerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public List<Wholesaler> getAllWholesalers() {
        return wholesalerRepository.findAll();
    }
    
    public Optional<Wholesaler> findByEmail(String email) {
    return wholesalerRepository.findByEmail(email);
    }


    public Wholesaler getWholesalerById(Long id) {
        return wholesalerRepository.findById(id).orElse(null);
    }

    // public Wholesaler saveWholesaler(Wholesaler wholesaler) {
    //     return wholesalerRepository.save(wholesaler);
    // }

    
    public Wholesaler saveWholesaler(Wholesaler updatedWholesaler) {
        Wholesaler existingWholesaler = wholesalerRepository.findById(updatedWholesaler.getId())
                .orElseThrow(() -> new RuntimeException("Wholesaler not found"));
        if (updatedWholesaler.getPassword() != null && !updatedWholesaler.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedWholesaler.getPassword());
            updatedWholesaler.setPassword(encodedPassword);
        } else {
            updatedWholesaler.setPassword(existingWholesaler.getPassword());
        }
        return wholesalerRepository.save(updatedWholesaler);
    }
    
    public void deleteWholesaler(Long id) {
        wholesalerRepository.deleteById(id);
    }

    public void deactivateWholesaler(Long id) {
        Wholesaler w = wholesalerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wholesaler not found"));
        w.setActive(false);
        wholesalerRepository.save(w);
    }




}
