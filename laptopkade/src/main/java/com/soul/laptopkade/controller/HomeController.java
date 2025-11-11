package com.soul.laptopkade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    // Small record to represent a laptop for the demo
    public record Laptop(String name, String brand, String price, String imageUrl) { }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Laptop> laptops = List.of(
                new Laptop("XPS 13", "Dell", "$999", "/images/laptops/laptop_183544.jpg"),
                new Laptop("MacBook Air", "Apple", "$1199", "/images/laptops/pexels-junior-teixeira-1064069-2047905.jpg"),
                new Laptop("ThinkPad X1", "Lenovo", "$1299", "/images/laptops/pexels-life-of-pix-7974.jpg"),
                new Laptop("Pavilion 15", "HP", "$849", "/images/laptops/pexels-pixabay-459653.jpg")
        );

        model.addAttribute("laptops", laptops);
        return "home"; // maps to src/main/resources/templates/home.html
    }

}
