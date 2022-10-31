package com.example.devcon.frontend.controllers;

import com.example.devcon.common.dto.ProductDto;
import com.example.devcon.common.domain.User;
import com.example.devcon.products.CategoryService;
import com.example.devcon.orders.OrderService;
import com.example.devcon.products.ProductService;
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
@RequestMapping("/")
public class IndexController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    public IndexController(ProductService productService,
                           CategoryService categoryService,
                           OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
    }

    @GetMapping
    public String list(ModelMap model) {
        final List<ProductDto> all = this.productService.findAll();
        model.addAttribute("products", all);
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.findAll());
        return "index";
    }

    @PostMapping("/products")
    @Secured("ROLE_ADMIN")
    public String addProduct(@Valid ProductDto product, BindingResult result) {
        if (result.hasErrors()) {
            return "index";
        }
        this.productService.create(product);
        return "redirect:/";
    }

    @GetMapping("/products/addProductToOrder/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addProductToOrder(Authentication auth, @PathVariable long id) {
        final User user = (User) auth.getPrincipal();
        this.orderService.addProductToOrder(user, id);
        return "redirect:/";
    }
}
