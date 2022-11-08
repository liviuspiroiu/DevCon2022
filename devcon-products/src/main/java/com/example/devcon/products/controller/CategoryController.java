package com.example.devcon.products.controller;

import com.example.devcon.common.dto.CategoryDto;
import com.example.devcon.products.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> list() {
        final List<CategoryDto> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }
}
