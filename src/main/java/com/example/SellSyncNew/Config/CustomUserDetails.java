
package com.example.SellSyncNew.Config;

import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Wholesaler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Object user; // Can be Admin, Manufacturer, or Wholesaler
    private final String role;

    private Admin admin;
    private Manufacturer manufacturer;
    private Wholesaler wholesaler;

    public CustomUserDetails(Object user, String role) {
        this.user = user;
        this.role = role;

        // 👇 Correctly map the actual object
        if (user instanceof Admin) {
            this.admin = (Admin) user;
        } else if (user instanceof Manufacturer) {
            this.manufacturer = (Manufacturer) user;
        } else if (user instanceof Wholesaler) {
            this.wholesaler = (Wholesaler) user;
        }
    }

    public Wholesaler getWholesaler() {return this.wholesaler;}

    public Manufacturer getManufacturer() {
        return this.manufacturer;
    }

    public Admin getAdmin() {
        return this.admin;
    }

    public Object getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        if (user instanceof Admin) return ((Admin) user).getPassword();
        if (user instanceof Manufacturer) return ((Manufacturer) user).getPassword();
        if (user instanceof Wholesaler) return ((Wholesaler) user).getPassword();
        return null;
    }

    @Override
    public String getUsername() {
        if (user instanceof Admin) return ((Admin) user).getEmail();
        if (user instanceof Manufacturer) return ((Manufacturer) user).getEmail();
        if (user instanceof Wholesaler) return ((Wholesaler) user).getEmail();
        return null;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
    String currentStatus = null;
    if (this.admin != null) currentStatus = this.admin.getStatus();
    else if (this.manufacturer != null) currentStatus = this.manufacturer.getStatus();
    else if (this.wholesaler != null) currentStatus = this.wholesaler.getStatus();

    System.out.println("DEBUG: User status is -> " + currentStatus);
    return "APPROVED".equalsIgnoreCase(currentStatus != null ? currentStatus.trim() : "");
    }

    // @Override
    // public boolean isEnabled() {
    //     if (user instanceof Admin) {
    //         return "APPROVED".equalsIgnoreCase(((Admin) user).getStatus());
    //     } else if (user instanceof Manufacturer) {
    //         return "APPROVED".equalsIgnoreCase(((Manufacturer) user).getStatus());
    //     } else if (user instanceof Wholesaler) {
    //         return "APPROVED".equalsIgnoreCase(((Wholesaler) user).getStatus());
    //     }
    //     return false;
    // }
}
