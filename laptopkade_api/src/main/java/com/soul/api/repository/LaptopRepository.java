package com.soul.api.repository;

import com.soul.api.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    List<Laptop> findByBrand(String brand);
    List<Laptop> findByModel(String model);
    List<Laptop> findByPriceLessThan(Double price);
    List<Laptop> findByPriceGreaterThan(Double price);
    
    // Custom queries
    @Query("SELECT l FROM Laptop l WHERE l.price BETWEEN :minPrice AND :maxPrice")
    List<Laptop> findLaptopsInPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT l FROM Laptop l WHERE l.stock > 0")
    List<Laptop> findLaptopsInStock();
    
    @Query("SELECT l FROM Laptop l WHERE l.stock <= 0")
    List<Laptop> findLaptopsOutOfStock();
    
    @Query("SELECT l FROM Laptop l WHERE LOWER(l.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.processor) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Laptop> searchLaptops(@Param("keyword") String keyword);
    
    @Query("SELECT AVG(l.price) FROM Laptop l")
    Optional<Double> getAveragePrice();
    
    @Query("SELECT l FROM Laptop l ORDER BY l.price DESC LIMIT 1")
    Optional<Laptop> getMostExpensiveLaptop();
    
    @Query("SELECT l FROM Laptop l ORDER BY l.price ASC LIMIT 1")
    Optional<Laptop> getLeastExpensiveLaptop();
}

