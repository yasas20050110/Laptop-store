package com.soul.api.service;

import com.soul.api.model.Laptop;
import com.soul.api.repository.LaptopRepository;
import com.soul.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LaptopService implements ILaptopService {
    
    @Autowired
    private LaptopRepository laptopRepository;
    
    /**
     * Create a new laptop
     */
    @Override
    public Laptop createLaptop(Laptop laptop) {
        if (laptop == null) {
            throw new IllegalArgumentException("Laptop cannot be null");
        }
        if (laptop.getPrice() == null || laptop.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be valid");
        }
        if (laptop.getStock() == null || laptop.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be valid");
        }
        return laptopRepository.save(laptop);
    }
    
    /**
     * Get all laptops
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }
    
    /**
     * Get laptop by ID
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Laptop> getLaptopById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid laptop ID");
        }
        return laptopRepository.findById(id);
    }
    
    /**
     * Get laptops by brand
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsByBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        return laptopRepository.findByBrand(brand);
    }
    
    /**
     * Get laptops by model
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsByModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
        return laptopRepository.findByModel(model);
    }
    
    /**
     * Get laptops under a certain price
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsUnderPrice(Double price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be valid");
        }
        return laptopRepository.findByPriceLessThan(price);
    }
    
    /**
     * Get laptops above a certain price
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsAbovePrice(Double price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be valid");
        }
        return laptopRepository.findByPriceGreaterThan(price);
    }
    
    /**
     * Get laptops in a price range
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsInPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Prices must be valid");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        return laptopRepository.findLaptopsInPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Search laptops by keyword
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> searchLaptops(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return laptopRepository.searchLaptops(keyword);
    }
    
    /**
     * Get laptops in stock
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsInStock() {
        return laptopRepository.findLaptopsInStock();
    }
    
    /**
     * Get laptops out of stock
     */
    @Override
    @Transactional(readOnly = true)
    public List<Laptop> getLaptopsOutOfStock() {
        return laptopRepository.findLaptopsOutOfStock();
    }
    
    /**
     * Update an existing laptop
     */
    @Override
    @SuppressWarnings("null")
    public Laptop updateLaptop(Long id, Laptop laptopDetails) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid laptop ID");
        }
        
        Laptop existingLaptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop", "id", id));
        
        if (laptopDetails != null) {
            if (laptopDetails.getBrand() != null && !laptopDetails.getBrand().isEmpty()) {
                existingLaptop.setBrand(laptopDetails.getBrand());
            }
            if (laptopDetails.getModel() != null && !laptopDetails.getModel().isEmpty()) {
                existingLaptop.setModel(laptopDetails.getModel());
            }
            if (laptopDetails.getProcessor() != null && !laptopDetails.getProcessor().isEmpty()) {
                existingLaptop.setProcessor(laptopDetails.getProcessor());
            }
            if (laptopDetails.getRam() != null && !laptopDetails.getRam().isEmpty()) {
                existingLaptop.setRam(laptopDetails.getRam());
            }
            if (laptopDetails.getStorage() != null && !laptopDetails.getStorage().isEmpty()) {
                existingLaptop.setStorage(laptopDetails.getStorage());
            }
            if (laptopDetails.getGraphicsCard() != null && !laptopDetails.getGraphicsCard().isEmpty()) {
                existingLaptop.setGraphicsCard(laptopDetails.getGraphicsCard());
            }
            if (laptopDetails.getPrice() != null && laptopDetails.getPrice() >= 0) {
                existingLaptop.setPrice(laptopDetails.getPrice());
            }
            if (laptopDetails.getStock() != null && laptopDetails.getStock() >= 0) {
                existingLaptop.setStock(laptopDetails.getStock());
            }
            if (laptopDetails.getDescription() != null && !laptopDetails.getDescription().isEmpty()) {
                existingLaptop.setDescription(laptopDetails.getDescription());
            }
        }
        return laptopRepository.save(existingLaptop);
    }
    
    /**
     * Update laptop stock
     */
    @Override
    public Laptop updateLaptopStock(Long id, Integer newStock) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid laptop ID");
        }
        if (newStock == null || newStock < 0) {
            throw new IllegalArgumentException("Stock must be valid");
        }
        
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop", "id", id));
        laptop.setStock(newStock);
        return laptopRepository.save(laptop);
    }
    
    /**
     * Update laptop price
     */
    @Override
    public Laptop updateLaptopPrice(Long id, Double newPrice) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid laptop ID");
        }
        if (newPrice == null || newPrice < 0) {
            throw new IllegalArgumentException("Price must be valid");
        }
        
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop", "id", id));
        laptop.setPrice(newPrice);
        return laptopRepository.save(laptop);
    }
    
    /**
     * Delete a laptop by ID
     */
    @Override
    public boolean deleteLaptop(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid laptop ID");
        }
        
        if (laptopRepository.existsById(id)) {
            laptopRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Delete all laptops
     */
    @Override
    public void deleteAllLaptops() {
        laptopRepository.deleteAll();
    }
    
    /**
     * Get total laptop count
     */
    @Override
    @Transactional(readOnly = true)
    public long getTotalLaptopCount() {
        return laptopRepository.count();
    }
    
    /**
     * Get average price of all laptops
     */
    @Override
    @Transactional(readOnly = true)
    public double getAveragePrice() {
        return laptopRepository.getAveragePrice().orElse(0.0);
    }
    
    /**
     * Get most expensive laptop
     */
    @Override
    @Transactional(readOnly = true)
    public Laptop getMostExpensiveLaptop() {
        return laptopRepository.getMostExpensiveLaptop()
                .orElseThrow(() -> new ResourceNotFoundException("No laptops found"));
    }
    
    /**
     * Get least expensive laptop
     */
    @Override
    @Transactional(readOnly = true)
    public Laptop getLeastExpensiveLaptop() {
        return laptopRepository.getLeastExpensiveLaptop()
                .orElseThrow(() -> new ResourceNotFoundException("No laptops found"));
    }
}

