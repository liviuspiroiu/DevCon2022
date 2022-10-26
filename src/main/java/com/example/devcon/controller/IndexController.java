package com.example.devcon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("message", "DevCon 2022");
        return "index";
    }

    @GetMapping("/hello/{name:.+}")
    public String hello(Model model, @PathVariable("name") String name) {
        model.addAttribute("message", name);
        return "index";
    }
}
