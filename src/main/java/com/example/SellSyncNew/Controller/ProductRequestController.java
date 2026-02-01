package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.DTO.ProductRequestDTO;
import com.example.SellSyncNew.DTO.ProductRequestResponse;
import com.example.SellSyncNew.Model.ProductRequest;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import com.example.SellSyncNew.Service.ProductRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Optional;


@RestController
@RequestMapping("/api/requests")
public class ProductRequestController {

    @Autowired
    private ProductRequestService productRequestService;

    @Autowired
    private WholesalerRepository wholesalerRepository;

    // ✅ Create new product request using logged-in wholesaler
    @PostMapping
    public ResponseEntity<String> submitRequest(@RequestBody ProductRequestDTO dto, Authentication authentication) {
        String email = authentication.getName(); // Logged-in user's email

        Optional<Wholesaler> optionalWholesaler = wholesalerRepository.findByEmail(email);
        if (optionalWholesaler.isEmpty()) {
            return ResponseEntity.badRequest().body("Wholesaler not found for email: " + email);
        }

        Wholesaler wholesaler = optionalWholesaler.get();

        ProductRequest request = new ProductRequest();
        request.setProductName(dto.productName);
        request.setCategory(dto.category);
        request.setManufacturer(dto.manufacturer);
        request.setQuantity(dto.quantity);
        request.setSpecifications(dto.specifications);
        request.setDeadline(dto.deadline);
        request.setStatus("Pending");
        request.setWholesaler(wholesaler); // ✅ Attach wholesaler

        productRequestService.save(request);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    // ✅ Get all requests (for admin or manufacturers)
    @GetMapping
    public List<ProductRequestResponse> getAllRequests() {
        return productRequestService.getAllWithWholesalerDetails();
    }

    // ✅ Update status (Approve/Reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ProductRequest request = productRequestService.getById(id);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        if (!status.equalsIgnoreCase("Approved") && !status.equalsIgnoreCase("Rejected")) {
            return ResponseEntity.badRequest().body("Invalid status");
        }

        request.setStatus(status);
        productRequestService.save(request);
        return ResponseEntity.ok("Status updated successfully");
    }

    // ✅ Get requests of the logged-in wholesaler
    @GetMapping("/my")
    public List<ProductRequestResponse> getMyRequests(Authentication authentication) {
        String email = authentication.getName();
        return productRequestService.getRequestsByWholesalerEmail(email);
    }
}
