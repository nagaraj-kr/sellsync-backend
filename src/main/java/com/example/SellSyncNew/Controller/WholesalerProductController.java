package com.example.SellSyncNew.Controller;



import com.example.SellSyncNew.DTO.ProductDTO;
import com.example.SellSyncNew.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wholesaler/products")
public class WholesalerProductController {

    @Autowired
    private ProductService productService;

//    @GetMapping("/browse")
//    public ResponseEntity<List<ProductDTO>> browseAllProducts() {
//        List<ProductDTO> products = productService. getAllProductsForWholesaler();
//        return ResponseEntity.ok(products);
//    }
@GetMapping("/browse")
public ResponseEntity<List<ProductDTO>> browseAllProducts() {
    List<ProductDTO> products = productService.getAllProductsForWholesaler(); // ✅ Already correct
    return ResponseEntity.ok(products);
}

}
