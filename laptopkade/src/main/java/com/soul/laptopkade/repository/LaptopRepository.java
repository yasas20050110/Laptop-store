package com.soul.laptopkade.repository;

import com.soul.laptopkade.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    
	@Query("SELECT l FROM Laptop l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
		   "OR LOWER(l.brand) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<Laptop> searchByNameOrBrand(@Param("query") String query);
}
