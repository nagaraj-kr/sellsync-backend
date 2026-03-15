package com.example.SellSyncNew.Controller;


import com.example.SellSyncNew.Config.CustomUserDetails;
import com.example.SellSyncNew.DTO.CheckoutRequest;
import com.example.SellSyncNew.DTO.OrderDTO;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Order;
import com.example.SellSyncNew.Model.Wholesaler;
import com.example.SellSyncNew.Repository.OrderRepository;
import com.example.SellSyncNew.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepo;

    @PostMapping("/checkout")
    public ResponseEntity<String> placeOrder(@RequestBody CheckoutRequest request, Authentication auth) {
        try {
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
            Wholesaler wholesaler = user.getWholesaler();
            orderService.createOrder(request, wholesaler);
            return ResponseEntity.ok("Order placed successfully!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }


    @GetMapping("/received")
    public ResponseEntity<List<Order>> getReceivedOrders(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Manufacturer manufacturer = user.getManufacturer();
        List<Order> orders = orderService.getOrdersForManufacturer(manufacturer);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();  // logged-in wholesaler's email/username
        List<OrderDTO> orderDTOs = orderService.getOrdersForLoggedInWholesaler(username);
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/manufacturer/orders")
    public ResponseEntity<List<OrderDTO>> getManufacturerOrders(Authentication authentication) {
        String email = authentication.getName(); // Get logged-in manufacturer's email
        List<OrderDTO> orders = orderService.getOrdersByManufacturerEmail(email);
        return ResponseEntity.ok(orders);
    }

    // @PutMapping("/{orderId}/status")
    // public ResponseEntity<OrderDTO> updateOrderStatus(
    //         @PathVariable Long orderId,
    //         @RequestBody Map<String, String> body) {

    //     String newStatus = body.get("status");
    //     OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
    //     return ResponseEntity.ok(updatedOrder);
    // }
       @PutMapping("/{orderId}/status")
        public ResponseEntity<OrderDTO> updateOrderStatus(
                @PathVariable Long orderId,
                @RequestBody Map<String, String> body) { // Request body-la "status" key irukkaal nu confirm pannanum
        
            String newStatus = body.get("status");
            if (newStatus == null) {
                return ResponseEntity.badRequest().build();
            }
            OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(updatedOrder);
        }


    @GetMapping("/my/pending")
    public ResponseEntity<List<OrderDTO>> getPendingOrders(Authentication authentication) {
        String username = authentication.getName(); // Logged-in wholesaler's email
        List<OrderDTO> pendingOrders = orderService.getPendingOrdersForLoggedInWholesaler(username);
        return ResponseEntity.ok(pendingOrders);
    }




}
