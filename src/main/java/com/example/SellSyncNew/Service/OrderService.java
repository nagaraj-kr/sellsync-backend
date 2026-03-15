//package com.example.SellSyncNew.Service;
//
//import com.example.SellSyncNew.DTO.CartItemDTO;
//import com.example.SellSyncNew.DTO.CheckoutRequest;
//import com.example.SellSyncNew.Model.*;
//import com.example.SellSyncNew.Repository.OrderRepository;
//import com.example.SellSyncNew.Repository.ProductRepository;
//import com.example.SellSyncNew.Repository.WholesalerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class OrderService {
//    @Autowired
//    private OrderRepository orderRepo;
//
//    @Autowired
//    private ProductRepository productRepo;
//
////    public Order createOrder(CheckoutRequest request, Wholesaler wholesaler) {
////        Order order = new Order();
////        order.setOrderDate(LocalDateTime.now());
////        order.setWholesaler(wholesaler);
////        order.setTotalAmount(request.getTotalAmount());
////
////        List<OrderItem> items = new ArrayList<>();
////        for (CartItemDTO dto : request.getCartItems()) {
////            Product product = productRepo.findById(dto.getProductId()).orElseThrow();
////            OrderItem item = new OrderItem();
////            item.setOrder(order);
////            item.setProduct(product);
////            item.setPrice(dto.getPrice());
////            item.setQuantity(dto.getQuantity());
////            items.add(item);
////        }
////
////        order.setItems(items);
////        return orderRepo.save(order);
////    }
//public Order createOrder(CheckoutRequest request, Wholesaler wholesaler) {
//    System.out.println("Creating order for wholesaler ID: " + (wholesaler != null ? wholesaler.getId() : "NULL"));
//
//    Order order = new Order();
//    order.setOrderDate(LocalDateTime.now());
//    order.setWholesaler(wholesaler);
//    order.setTotalAmount(request.getTotalAmount());
//
//    List<OrderItem> items = new ArrayList<>();
//
//    for (CartItemDTO dto : request.getCartItems()) {
//        Product product = productRepo.findById(dto.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + dto.getProductId()));
//
//        System.out.println("Adding item: productId=" + product.getId() + ", quantity=" + dto.getQuantity());
//
//        OrderItem item = new OrderItem();
//        item.setOrder(order);
//        item.setProduct(product);
//        item.setQuantity(dto.getQuantity());
//        item.setPrice(dto.getPrice());
//        items.add(item);
//    }
//
//    order.setItems(items);
//    return orderRepo.save(order);
//}
//
//
//    public List<Order> getOrdersForWholesaler(Wholesaler wholesaler) {
//        return orderRepo.findByWholesaler(wholesaler);
//    }
//
//    public List<Order> getOrdersForManufacturer(Manufacturer manufacturer) {
//        return orderRepo.findOrdersByManufacturer(manufacturer);
//    }
//}
package com.example.SellSyncNew.Service;

import com.example.SellSyncNew.DTO.CartItemDTO;
import com.example.SellSyncNew.DTO.CheckoutRequest;
import com.example.SellSyncNew.DTO.OrderDTO;
import com.example.SellSyncNew.Model.*;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Repository.OrderRepository;
import com.example.SellSyncNew.Repository.ProductRepository;
import com.example.SellSyncNew.Repository.WholesalerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ManufacturerRepository manufacturerRepo;

    @Autowired
    private WholesalerRepository wholesalerRepo;

    public Order createOrder(CheckoutRequest request, Wholesaler wholesaler) {
        if (wholesaler == null) {
            throw new RuntimeException("Wholesaler is not authenticated.");
        }

        System.out.println("Creating order for wholesaler ID: " + wholesaler.getId());

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setWholesaler(wholesaler);
        order.setTotalAmount(request.getTotalAmount());
        order.setOrderStatus("Pending");

        List<OrderItem> items = new ArrayList<>();

        Manufacturer commonManufacturer = null;

        for (CartItemDTO dto : request.getCartItems()) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: ID " + dto.getProductId()));

            Manufacturer manufacturer = product.getManufacturer();
            if (manufacturer == null) {
                throw new RuntimeException("Product ID " + product.getId() + " is missing a manufacturer.");
            }

            if (commonManufacturer == null) {
                commonManufacturer = manufacturer; // First manufacturer
            } else if (!commonManufacturer.getId().equals(manufacturer.getId())) {
                throw new RuntimeException("All products in an order must belong to the same manufacturer.");
            }

            System.out.println("Adding product " + product.getId() +
                    " with manufacturer ID " + manufacturer.getId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());

            items.add(item);
        }

        // ✅ Set the manufacturer on the order
        order.setManufacturer(commonManufacturer);
        order.setItems(items);
        return orderRepo.save(order);
    }

    public List<Order> getOrdersForWholesaler(Wholesaler wholesaler) {
        return orderRepo.findByWholesaler(wholesaler);
    }

    public List<Order> getOrdersForManufacturer(Manufacturer manufacturer) {
        return orderRepo.findOrdersByManufacturer(manufacturer);
    }

    public List<OrderDTO> getOrdersForLoggedInWholesaler(String username) {
        Wholesaler wholesaler = wholesalerRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Wholesaler not found"));

        List<Order> orders = orderRepo.findByWholesaler(wholesaler);

        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getId());
            dto.setOrderDate(order.getOrderDate().toLocalDate().toString());

            // ✅ Supplier Name Logic with Fallback
            String supplierName = "Unknown Supplier";
            if (order.getManufacturer() != null) {
                if (order.getManufacturer().getCompanyName() != null && !order.getManufacturer().getCompanyName().isBlank()) {
                    supplierName = order.getManufacturer().getCompanyName();
                } else if (order.getManufacturer().getOrganizationName() != null) {
                    supplierName = order.getManufacturer().getOrganizationName();
                }
            }
            dto.setSupplierName(supplierName);

            dto.setItemCount(order.getItems().size());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setOrderStatus(order.getOrderStatus());

            return dto;
        }).collect(Collectors.toList());
    }


//
public List<OrderDTO> getOrdersByManufacturerEmail(String email) {
    Manufacturer manufacturer = manufacturerRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Manufacturer not found"));

    List<Order> orders = orderRepo.findByManufacturer(manufacturer);

    return orders.stream().map(order -> {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate().toLocalDate().toString());

        // Set buyer name (Wholesaler)
        String buyerName = "Unknown Buyer";
        if (order.getWholesaler() != null) {
            if (order.getWholesaler().getOrganizationName() != null && !order.getWholesaler().getOrganizationName().isBlank()) {
                buyerName = order.getWholesaler().getOrganizationName();
            } else if (order.getWholesaler().getOrganizationName() != null && !order.getWholesaler().getOrganizationName().isBlank()) {
                buyerName = order.getWholesaler().getOrganizationName();
            }
        }
        dto.setBuyerName(buyerName);

        // Set supplier name (Manufacturer)
        String supplierName = "Unknown Supplier";
        if (manufacturer.getCompanyName() != null && !manufacturer.getCompanyName().isBlank()) {
            supplierName = manufacturer.getCompanyName();
        } else if (manufacturer.getOrganizationName() != null) {
            supplierName = manufacturer.getOrganizationName();
        }
        dto.setSupplierName(supplierName);

        dto.setItemCount(order.getItems().size());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());


        if (!order.getItems().isEmpty()) {
            Product product = order.getItems().get(0).getProduct();
            dto.setProductName(product != null ? product.getName() : "Unknown Product");
        } else {
            dto.setProductName("No Product");
        }

        return dto;
    }).collect(Collectors.toList());
}

    @Autowired
    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    // ✅ Business logic to update the order status
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(newStatus);
        orderRepo.save(order);

        return convertToDTO(order); // convert to OrderDTO
    }
 
 
    // ✅ Convert Order to OrderDTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate().toLocalDate().toString());

        String buyerName = "Unknown Buyer";
        if (order.getWholesaler() != null && order.getWholesaler().getOrganizationName() != null) {
            buyerName = order.getWholesaler().getOrganizationName();
        }
        if (!order.getItems().isEmpty() && order.getItems().get(0).getProduct() != null) {
            dto.setProductName(order.getItems().get(0).getProduct().getName());
        } else {
            dto.setProductName("N/A");
        }

        dto.setSupplierName(buyerName);
        dto.setItemCount(order.getItems().size());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());

        return dto;
    }
    public List<OrderDTO> getPendingOrdersForLoggedInWholesaler(String username) {
        Wholesaler wholesaler = wholesalerRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Wholesaler not found"));

        List<Order> orders = orderRepo.findByWholesalerAndOrderStatus(wholesaler, "Pending");

        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}
