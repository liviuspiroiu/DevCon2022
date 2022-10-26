package com.example.devcon.controller;

import com.example.devcon.dto.ProductDto;
import com.example.devcon.service.CategoryService;
import com.example.devcon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.devcon.model.enumeration.ProductStatus.AVAILABLE;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/")
    public String findAll(ModelMap model) {
        List<ProductDto> all = this.productService.findAll();
        model.addAttribute("products", all);
        return "products";
    }

    @GetMapping(value = "/add-product")
    public String add(ModelMap model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.findAll());
        return "product";
    }

    @GetMapping("/{id}")
    public String findById(ModelMap model, @PathVariable Long id) {
        ProductDto product = this.productService.findById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping
    public String addUser(@Valid ProductDto product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "product";
        }
        this.productService.create(product);
        return "redirect:/products/";
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.productService.delete(id);
    }
}
