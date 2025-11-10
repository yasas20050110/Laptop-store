package com.soul.laptopkade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    // Small record to represent a laptop for the demo
    public record Laptop(String name, String brand, String price) { }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Laptop> laptops = List.of(
                new Laptop("XPS 13", "Dell", "$999"),
                new Laptop("MacBook Air", "Apple", "$1199"),
                new Laptop("ThinkPad X1", "Lenovo", "$1299")
        );

        model.addAttribute("laptops", laptops);
        return "home"; // maps to src/main/resources/templates/home.html
    }

}
