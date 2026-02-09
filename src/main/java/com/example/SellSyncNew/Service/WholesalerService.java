package com.example.SellSyncNew.Service;


import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WholesalerService {

    @Autowired
    private WholesalerRepository wholesalerRepository;

    public List<Wholesaler> getAllWholesalers() {
        return wholesalerRepository.findAll();
    }
    
    public Optional<Wholesaler> findByEmail(String email) {
    return wholesalerRepository.findByEmail(email);
    }


    public Wholesaler getWholesalerById(Long id) {
        return wholesalerRepository.findById(id).orElse(null);
    }

    public Wholesaler saveWholesaler(Wholesaler wholesaler) {
        return wholesalerRepository.save(wholesaler);
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
