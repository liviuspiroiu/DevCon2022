package com.example.devcon.controller;

import com.example.devcon.dto.ProductDto;
import com.example.devcon.model.User;
import com.example.devcon.service.CategoryService;
import com.example.devcon.service.OrderService;
import com.example.devcon.service.ProductService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/")
    public String findAll(ModelMap model) {
        final List<ProductDto> all = this.productService.findAll();
        model.addAttribute("products", all);
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.findAll());
        return "/products/list";
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public String addProduct(@Valid ProductDto product, BindingResult result) {
        if (result.hasErrors()) {
            return "/products/list";
        }
        this.productService.create(product);
        return "redirect:/products/";
    }

    @GetMapping("/addProductToOrder/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addProductToOrder(Authentication auth, @PathVariable long id) {
        final User user = (User) auth.getPrincipal();
        this.orderService.addProductToOrder(user, id);
        return "redirect:/products/";
    }
}
