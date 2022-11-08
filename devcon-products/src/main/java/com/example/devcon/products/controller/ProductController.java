package com.example.devcon.products.controller;

import com.example.devcon.common.dto.ProductDto;
import com.example.devcon.products.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> list() {
        final List<ProductDto> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/")
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.create(productDto));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable long id) {
        productService.delete(id);
        return ResponseEntity.ok(true);
    }
}
