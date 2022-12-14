package com.example.devcon.frontend.controllers;

import com.example.devcon.common.dto.ProductDto;
import com.example.devcon.frontend.service.OrderService;
import com.example.devcon.frontend.service.ProductService;
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
    private final OrderService orderService;

    public IndexController(ProductService productService,
                           OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    public String list(ModelMap model) {
        final List<ProductDto> all = this.productService.findAll();
        model.addAttribute("products", all);
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", productService.findAllCategories());
        return "index";
    }

    @PostMapping("/products")
    public String addProduct(@Valid ProductDto product, BindingResult result) {
        if (result.hasErrors()) {
            return "index";
        }
        this.productService.create(product);
        return "redirect:/";
    }

    @GetMapping("/products/addProductToOrder/{id}")
    public String addProductToOrder(@PathVariable long id) {
        this.orderService.addProductToOrder(id);
        return "redirect:/";
    }
}
