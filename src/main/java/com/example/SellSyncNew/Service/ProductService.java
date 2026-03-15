//package com.example.SellSyncNew.Service;
//
//
//
//import com.example.SellSyncNew.DTO.ProductDTO;
//import com.example.SellSyncNew.Model.Manufacturer;
//import com.example.SellSyncNew.Model.Product;
//import com.example.SellSyncNew.Repository.ManufacturerRepository;
//import com.example.SellSyncNew.Repository.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.util.UUID;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final ProductRepository repository;
//
//    private final ManufacturerRepository manufacturerRepository;
//
//public String saveImage(MultipartFile file) throws IOException {
//    String uploadDir = "uploads/";
//    File directory = new File(uploadDir);
//    if (!directory.exists()) {
//        directory.mkdirs(); // Create folder if it doesn’t exist
//    }
//
//    String originalFilename = file.getOriginalFilename();
//    String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
//    File destination = new File(uploadDir + uniqueFilename);
//
//    file.transferTo(destination); // Save the file to disk
//
//    return uniqueFilename; // return just the file name
//}
//
//
//    public Product addProduct(ProductDTO dto) {
//        // ✅ Get logged-in user's email
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // ✅ Lookup manufacturer by email
//        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Manufacturer not found for email: " + email));
//
//        if (manufacturer == null) {
//            throw new RuntimeException("Manufacturer not found for email: " + email);
//        }
//
//        // ✅ Save image to disk
//        String imageUrl = null;
//        MultipartFile imageFile = dto.getImage();
//        if (imageFile != null && !imageFile.isEmpty()) {
//            try {
//                String originalFilename = imageFile.getOriginalFilename().replaceAll("\\s+", "_");
//                String filename = UUID.randomUUID() + "_" + originalFilename;
//                Path path = Paths.get("uploads", filename);
//                Files.createDirectories(path.getParent());
//                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                imageUrl = "/uploads/" + filename;
//                System.out.println("Saved file to: " + path.toAbsolutePath());
//            } catch (IOException e) {
//                throw new RuntimeException("Image upload failed", e);
//            }
//        }
//
//        // ✅ Build and save product
//        Product product = Product.builder()
//                .name(dto.getName())
//                .category(dto.getCategory())
//                .price(dto.getPrice())
//                .stock(dto.getStock())
//                .description(dto.getDescription())
//                .imageUrl(imageUrl)
//                .manufacturer(manufacturer)
//                .build();
//        System.out.println("Saved product image URL: " + imageUrl);
//
//
//
//        return repository.save(product);
//    }
//
//// commend code
////    public List<ProductDTO> getProductsForCurrentManufacturer() {
////        String email = SecurityContextHolder.getContext().getAuthentication().getName();
////
////        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
////
////        List<Product> products = repository.findByManufacturerId(manufacturer.getId());
////
////        return products.stream().map(product -> {
////            ProductDTO dto = new ProductDTO();
////            dto.setId(product.getId());
////            dto.setName(product.getName());
////            dto.setCategory(product.getCategory());
////            dto.setPrice(product.getPrice());
////            dto.setStock(product.getStock());
////            dto.setDescription(product.getDescription());
////            dto.setImageUrl(product.getImageUrl());
////            return dto;
////        }).collect(Collectors.toList());
////    }
//
//
//    public List<ProductDTO> getProductsForCurrentManufacturer() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
//
//        // ✅ Only get products with status "active"
//        List<Product> products = repository.findByManufacturerIdAndStatusIgnoreCase(manufacturer.getId(), "active");
//
//        return products.stream().map(product -> {
//            ProductDTO dto = new ProductDTO();
//            dto.setId(product.getId());
//            dto.setName(product.getName());
//            dto.setCategory(product.getCategory());
//            dto.setPrice(product.getPrice());
//            dto.setStock(product.getStock());
//            dto.setDescription(product.getDescription());
//            dto.setImageUrl(product.getImageUrl());
//            dto.setStatus(product.getStatus());
//            return dto;
//        }).collect(Collectors.toList());
//    }
//
//// commend code
//    //    public List<Product> getAllProducts() {
////        return repository.findAll();
////    }
//
//
//
//public List<ProductDTO> getAllProductsForWholesaler() {
//    List<Product> products = repository.findByStatus("active");
//
//    return products.stream()
//            .map(product -> ProductDTO.builder()
//                    .id(product.getId())
//                    .name(product.getName())
//                    .category(product.getCategory())
//                    .price(product.getPrice())
//                    .description(product.getDescription())
//                    .imageUrl(product.getImageUrl())
//                    .manufacturerName(product.getManufacturer().getCompanyName())
//                    .build())
//            .toList();
//}
//
//
//// commend code
//    //    public Product getProduct(Long id) {
////        return repository.findById(id).orElse(null);
////    }
//
//public Product getProduct(Long id) {
//    String email = SecurityContextHolder.getContext().getAuthentication().getName();
//    Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
//            .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
//
//    return repository.findById(id)
//            .filter(p -> p.getManufacturer().getId().equals(manufacturer.getId()))
//            .orElseThrow(() -> new RuntimeException("Product not found or access denied"));
//}
//
//
//
//    public Product updateProduct(Long id, ProductDTO dto) {
//        Product product = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Update basic fields
//        product.setName(dto.getName());
//        product.setCategory(dto.getCategory());
//        product.setPrice(dto.getPrice());
//        product.setStock(dto.getStock());
//        product.setDescription(dto.getDescription());
//
//        // Handle image upload
//        MultipartFile newImage = dto.getImage();
//        if (newImage != null && !newImage.isEmpty()) {
//            try {
//                String imagePath = saveImage(newImage); // call image save method
//                product.setImageUrl(imagePath);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
//            }
//        }
//
//        return repository.save(product);
//    }
//
//
////    public void deleteProduct(Long id) {
////        repository.deleteById(id);
////    }
//public void pauseProduct(Long id) {
//    Product product = repository.findById(id)
//            .orElseThrow(() -> new RuntimeException("Product not found"));
//    product.setStatus("paused");
//    repository.save(product);
//}
//
//// commend code
//    // Updated to return List<ProductDTO>
////    public List<ProductDTO> getAllProductsForWholesaler() {
////        List<Product> products = repository.findAll();
////        return products.stream()
////                .map(this::convertToDTO)  // Convert Product to ProductDTO
////                .collect(Collectors.toList());
////    }
//
//    private ProductDTO convertToDTO(Product product) {
//        ProductDTO dto = new ProductDTO();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setCategory(product.getCategory());
//        dto.setDescription(product.getDescription());
//        dto.setPrice(product.getPrice());
//
//        if (product.getManufacturer() != null) {
//            dto.setManufacturerName(product.getManufacturer().getOrganizationName());
//        }
//
//        dto.setImageUrl(product.getImageUrl());  // Assuming this is present
//
//        return dto;
//    }
//
//}

package com.example.SellSyncNew.Service;

import com.example.SellSyncNew.DTO.ProductDTO;
import com.example.SellSyncNew.Model.Manufacturer;
import com.example.SellSyncNew.Model.Product;
import com.example.SellSyncNew.Repository.ManufacturerRepository;
import com.example.SellSyncNew.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ManufacturerRepository manufacturerRepository;

    // private final String UPLOAD_DIR = "uploads";

    // public String saveImage(MultipartFile file) throws IOException {
    //     File directory = new File(UPLOAD_DIR);
    //     if (!directory.exists()) directory.mkdirs();

    //     String originalFilename = file.getOriginalFilename().replaceAll("\\s+", "_");
    //     String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
    //     File destination = new File(directory, uniqueFilename);

    //     file.transferTo(destination);
    //     System.out.println("✅ File saved at: " + destination.getAbsolutePath());

    //     return "/uploads/" + uniqueFilename;
    // }


public Product addProduct(ProductDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found for email: " + email));

        // ⭐ Base64 String-ah direct-ah DTO-la irundhu vangurom
        String imageUrl = dto.getImageUrl();

        Product product = Product.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .imageUrl(imageUrl) // ⭐ Direct-ah Base64 string save aagum
                .status("active")
                .manufacturer(manufacturer)
                .build();

        return repository.save(product);
    }

    /**
     * Update an existing product.
     */
    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());

        // ⭐ Update logic-laiyum Base64 string-ah direct-ah update pannunga
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            product.setImageUrl(dto.getImageUrl());
        }

        return repository.save(product);
    }
    /**
     * Get all products for the logged-in manufacturer.
     */
    public List<ProductDTO> getProductsForCurrentManufacturer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));

        List<Product> products = repository.findByManufacturerIdAndStatusIgnoreCase(manufacturer.getId(), "active");

        return products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setCategory(product.getCategory());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setDescription(product.getDescription());
            dto.setImageUrl(product.getImageUrl());
            dto.setStatus(product.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Get all active products for wholesalers.
     */
    public List<ProductDTO> getAllProductsForWholesaler() {
        List<Product> products = repository.findByStatus("active");
        return products.stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .manufacturerName(product.getManufacturer().getOrganizationName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get a product for editing by manufacturer.
     */
    public Product getProduct(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Manufacturer manufacturer = manufacturerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));

        return repository.findById(id)
                .filter(p -> p.getManufacturer().getId().equals(manufacturer.getId()))
                .orElseThrow(() -> new RuntimeException("Product not found or access denied"));
    }

    /**
     * Update an existing product.
     */
    // public Product updateProduct(Long id, ProductDTO dto) {
    //     Product product = repository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Product not found"));

    //     // Update fields
    //     product.setName(dto.getName());
    //     product.setCategory(dto.getCategory());
    //     product.setPrice(dto.getPrice());
    //     product.setStock(dto.getStock());
    //     product.setDescription(dto.getDescription());

    //     // Handle new image
    //     MultipartFile newImage = dto.getImage();
    //     if (newImage != null && !newImage.isEmpty()) {
    //         try {
    //             String imagePath = saveImage(newImage);
    //             product.setImageUrl(imagePath);
    //         } catch (IOException e) {
    //             throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
    //         }
    //     }

    //     return repository.save(product);
    // }

    /**
     * Pause product (soft delete).
     */
    public void pauseProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus("paused");
        repository.save(product);
    }

    /**
     * Helper to convert Product to DTO (if needed)
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        if (product.getManufacturer() != null) {
            dto.setManufacturerName(product.getManufacturer().getOrganizationName());
        }
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
}
