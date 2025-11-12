package com.soul.laptopkade.controller;

import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.LaptopRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final LaptopRepository laptopRepository;

    public HomeController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        try {
            logger.info("READ operation: Fetching all laptops from database");
        List<Laptop> laptops = laptopRepository.findAll();
            logger.info("READ operation successful: Retrieved {} laptops", laptops.size());
        model.addAttribute("laptops", laptops);
        return "home"; // maps to src/main/resources/templates/home.html
        } catch (Exception e) {
            logger.error("READ operation failed: Error fetching laptops", e);
            throw e;
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", defaultValue = "") String query, Model model) {
        try {
            logger.info("SEARCH operation: Searching for laptops with query: '{}'", query);
            List<Laptop> results;
            
            if (query.isEmpty()) {
                results = laptopRepository.findAll();
            } else {
                results = laptopRepository.searchByNameOrBrand(query);
            }
            
            logger.info("SEARCH operation successful: Found {} laptops matching '{}'", results.size(), query);
            model.addAttribute("laptops", results);
            model.addAttribute("query", query);
                return "search-results :: laptop-list";
        } catch (Exception e) {
            logger.error("SEARCH operation failed: Error during search for '{}'", query, e);
            throw e;
        }
    }

}
