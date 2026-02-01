package com.example.SellSyncNew.Controller;

import com.example.SellSyncNew.DTO.DashboardSummaryDTO;
import com.example.SellSyncNew.Model.Admin;
import com.example.SellSyncNew.Model.Order;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.*;
import com.example.SellSyncNew.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private  AdminRepository adminRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private WholesalerRepository wholesalerRepository;
    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/dashboard-summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }


    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInAdminUsername() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Admin> admin = adminRepo.findByEmail(email);
        if (admin.isPresent()) {
            return ResponseEntity.ok(Map.of("username", admin.get().getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
    }

    @GetMapping("/dashboard-data")
    public ResponseEntity<?> getDashboardData() {
        // Get top 4 recent orders
        List<Order> recentOrders = orderRepository.findTop4ByOrderByOrderDateDesc();

        // Map each order to include wholesaler name
        List<Map<String, Object>> recentOrderDTOs = recentOrders.stream().map(order -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", order.getId());
            map.put("totalAmount", order.getTotalAmount());
            map.put("orderStatus", order.getOrderStatus());

            // Fetch wholesaler name based on wholesaler_id
            Optional<Wholesaler> wholesalerOpt = wholesalerRepository.findById(order.getWholesaler().getId());

            String wholesalerName = wholesalerOpt.map(Wholesaler::getOrganizationName).orElse("N/A");

            map.put("wholesalerName", wholesalerName);
            return map;
        }).collect(Collectors.toList());

        // Top manufacturers list
        List<Map<String, Object>> topManufacturers = manufacturerRepository.findAll().stream()
                .map(m -> {
                    long productCount = productRepository.countByManufacturerId(m.getId());
                    long orderCount = orderRepository.countByManufacturerId(m.getId());

                    Map<String, Object> map = new HashMap<>();
                    map.put("manufacturer", m.getOrganizationName());
                    map.put("products", productCount);
                    map.put("orders", orderCount);
                    return map;
                })
                .sorted((a, b) -> Long.compare((Long) b.get("orders"), (Long) a.get("orders")))
                .limit(4)
                .collect(Collectors.toList());

        // Final JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("recentOrders", recentOrderDTOs);
        response.put("topManufacturers", topManufacturers);

        return ResponseEntity.ok(response);
    }



}
