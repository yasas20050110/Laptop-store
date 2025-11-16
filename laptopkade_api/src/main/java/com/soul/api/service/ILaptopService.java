package com.soul.api.service;

import com.soul.api.model.Laptop;
import java.util.List;
import java.util.Optional;

public interface ILaptopService {
    
    // Create
    Laptop createLaptop(Laptop laptop);
    
    // Read
    List<Laptop> getAllLaptops();
    Optional<Laptop> getLaptopById(Long id);
    List<Laptop> getLaptopsByBrand(String brand);
    List<Laptop> getLaptopsByModel(String model);
    List<Laptop> getLaptopsUnderPrice(Double price);
    List<Laptop> getLaptopsAbovePrice(Double price);
    List<Laptop> getLaptopsInPriceRange(Double minPrice, Double maxPrice);
    List<Laptop> searchLaptops(String keyword);
    List<Laptop> getLaptopsInStock();
    List<Laptop> getLaptopsOutOfStock();
    
    // Update
    Laptop updateLaptop(Long id, Laptop laptopDetails);
    Laptop updateLaptopStock(Long id, Integer newStock);
    Laptop updateLaptopPrice(Long id, Double newPrice);
    
    // Delete
    boolean deleteLaptop(Long id);
    void deleteAllLaptops();
    
    // Additional operations
    long getTotalLaptopCount();
    double getAveragePrice();
    Laptop getMostExpensiveLaptop();
    Laptop getLeastExpensiveLaptop();
}
