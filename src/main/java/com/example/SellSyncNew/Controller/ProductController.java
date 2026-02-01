package com.example.SellSyncNew.Controller;


import com.example.SellSyncNew.DTO.ProductDTO;
import com.example.SellSyncNew.Model.Product;
import com.example.SellSyncNew.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> add(@ModelAttribute ProductDTO dto) {
        Product saved = productService.addProduct(dto);

        ProductDTO response = new ProductDTO();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setCategory(saved.getCategory());
        response.setPrice(saved.getPrice());
        response.setStock(saved.getStock());
        response.setDescription(saved.getDescription());
        response.setImageUrl(saved.getImageUrl());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getProductsForCurrentManufacturer());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> update(@PathVariable Long id, @ModelAttribute ProductDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<Void> pauseProduct(@PathVariable Long id) {
        productService.pauseProduct(id);
        return ResponseEntity.noContent().build();
    }

}
