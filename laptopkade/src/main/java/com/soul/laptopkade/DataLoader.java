package com.soul.laptopkade;

import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.LaptopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(LaptopRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Laptop("XPS 13", "Dell", "$999", "/images/laptops/laptop_183544.jpg"));
                repo.save(new Laptop("MacBook Air", "Apple", "$1199", "/images/laptops/pexels-junior-teixeira-1064069-2047905.jpg"));
                repo.save(new Laptop("ThinkPad X1", "Lenovo", "$1299", "/images/laptops/pexels-life-of-pix-7974.jpg"));
                repo.save(new Laptop("Pavilion 15", "HP", "$849", "/images/laptops/pexels-pixabay-459653.jpg"));
            }
        };
    }

}
