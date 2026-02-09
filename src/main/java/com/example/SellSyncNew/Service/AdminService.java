package com.example.SellSyncNew.Service;

import com.example.SellSyncNew.DTO.AdminPasswordChangeDTO;
import com.example.SellSyncNew.DTO.AdminUpdateProfileDTO;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Admin> findByEmail(String email) {
    return adminRepository.findByEmail(email);
}


    public Admin updateProfile(AdminUpdateProfileDTO dto) {
        Admin admin = adminRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());

        return adminRepository.save(admin);
    }



    public void changePasswordByEmail(String email, AdminPasswordChangeDTO dto) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), admin.getPassword())) {
            throw new RuntimeException("Incorrect current password");
        }

        admin.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }


    public Admin getAdmin(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
    }
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

}
