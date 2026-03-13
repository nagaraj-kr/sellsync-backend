package com.example.SellSyncNew.Service;



import com.example.SellSyncNew.Config.CustomUserDetails;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.AdminRepository;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


import java.util.Optional;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private WholesalerRepository wholesalerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check Admin table
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(admin, "ADMIN");
        }

        // Check Manufacturer table
        Optional<Manufacturer> manOpt = manufacturerRepository.findByEmail(email);
        if (manOpt.isPresent()) {
            Manufacturer man = manOpt.get();
            return new CustomUserDetails(man, "MANUFACTURER");
        }

        // Check Wholesaler table
        Optional<Wholesaler> wholeOpt = wholesalerRepository.findByEmail(email);
        if (wholeOpt.isPresent()) {
            Wholesaler whole = wholeOpt.get();
            return new CustomUserDetails(whole, "WHOLESALER");
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}
