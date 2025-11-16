package com.soul.api.controller;

import com.soul.api.model.Laptop;
import com.soul.api.service.ILaptopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/laptops")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LaptopController {
    
    @Autowired
    private ILaptopService laptopService;
    
    // ==================== CREATE ====================
    @PostMapping
    public ResponseEntity<Laptop> createLaptop(@RequestBody Laptop laptop) {
        Laptop createdLaptop = laptopService.createLaptop(laptop);
        return new ResponseEntity<>(createdLaptop, HttpStatus.CREATED);
    }
    
    // ==================== READ ====================
    @GetMapping
    public ResponseEntity<List<Laptop>> getAllLaptops() {
        List<Laptop> laptops = laptopService.getAllLaptops();
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Laptop> getLaptopById(@PathVariable Long id) {
        Optional<Laptop> laptop = laptopService.getLaptopById(id);
        if (laptop.isPresent()) {
            return new ResponseEntity<>(laptop.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Laptop>> getLaptopsByBrand(@PathVariable String brand) {
        List<Laptop> laptops = laptopService.getLaptopsByBrand(brand);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/model/{model}")
    public ResponseEntity<List<Laptop>> getLaptopsByModel(@PathVariable String model) {
        List<Laptop> laptops = laptopService.getLaptopsByModel(model);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/price/under/{price}")
    public ResponseEntity<List<Laptop>> getLaptopsUnderPrice(@PathVariable Double price) {
        List<Laptop> laptops = laptopService.getLaptopsUnderPrice(price);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/price/above/{price}")
    public ResponseEntity<List<Laptop>> getLaptopsAbovePrice(@PathVariable Double price) {
        List<Laptop> laptops = laptopService.getLaptopsAbovePrice(price);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/price/range")
    public ResponseEntity<List<Laptop>> getLaptopsInPriceRange(
            @RequestParam Double minPrice, 
            @RequestParam Double maxPrice) {
        List<Laptop> laptops = laptopService.getLaptopsInPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Laptop>> searchLaptops(@RequestParam String keyword) {
        List<Laptop> laptops = laptopService.searchLaptops(keyword);
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/in-stock")
    public ResponseEntity<List<Laptop>> getLaptopsInStock() {
        List<Laptop> laptops = laptopService.getLaptopsInStock();
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Laptop>> getLaptopsOutOfStock() {
        List<Laptop> laptops = laptopService.getLaptopsOutOfStock();
        return new ResponseEntity<>(laptops, HttpStatus.OK);
    }
    
    // ==================== STATISTICS ====================
    @GetMapping("/stats/total-count")
    public ResponseEntity<Long> getTotalLaptopCount() {
        long count = laptopService.getTotalLaptopCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping("/stats/average-price")
    public ResponseEntity<Double> getAveragePrice() {
        double avgPrice = laptopService.getAveragePrice();
        return new ResponseEntity<>(avgPrice, HttpStatus.OK);
    }
    
    @GetMapping("/stats/most-expensive")
    public ResponseEntity<Laptop> getMostExpensiveLaptop() {
        Laptop laptop = laptopService.getMostExpensiveLaptop();
        return new ResponseEntity<>(laptop, HttpStatus.OK);
    }
    
    @GetMapping("/stats/least-expensive")
    public ResponseEntity<Laptop> getLeastExpensiveLaptop() {
        Laptop laptop = laptopService.getLeastExpensiveLaptop();
        return new ResponseEntity<>(laptop, HttpStatus.OK);
    }
    
    // ==================== UPDATE ====================
    @PutMapping("/{id}")
    public ResponseEntity<Laptop> updateLaptop(@PathVariable Long id, @RequestBody Laptop laptopDetails) {
        Laptop updatedLaptop = laptopService.updateLaptop(id, laptopDetails);
        return new ResponseEntity<>(updatedLaptop, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Laptop> updateLaptopStock(@PathVariable Long id, @RequestParam Integer stock) {
        Laptop updatedLaptop = laptopService.updateLaptopStock(id, stock);
        return new ResponseEntity<>(updatedLaptop, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/price")
    public ResponseEntity<Laptop> updateLaptopPrice(@PathVariable Long id, @RequestParam Double price) {
        Laptop updatedLaptop = laptopService.updateLaptopPrice(id, price);
        return new ResponseEntity<>(updatedLaptop, HttpStatus.OK);
    }
    
    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaptop(@PathVariable Long id) {
        boolean deleted = laptopService.deleteLaptop(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteAllLaptops() {
        laptopService.deleteAllLaptops();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // ==================== HEALTH CHECK ====================
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Laptop API is running", HttpStatus.OK);
    }
}

