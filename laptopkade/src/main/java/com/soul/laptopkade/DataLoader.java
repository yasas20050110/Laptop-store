package com.soul.laptopkade;

import com.soul.laptopkade.model.Admin;
import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.AdminRepository;
import com.soul.laptopkade.repository.LaptopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(LaptopRepository laptopRepo, AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed admin users if none exist
            if (adminRepo.count() == 0) {
                // Create default admin user with BCrypt encoded password
                Admin admin = new Admin("admin", passwordEncoder.encode("admin123"), "admin@laptopstore.com");
                adminRepo.save(admin);
            }

            // Seed laptop data if none exist
            if (laptopRepo.count() == 0) {
                laptopRepo.save(new Laptop("XPS 13", "Dell", "$999", "/images/laptops/laptop_183544.jpg"));
                laptopRepo.save(new Laptop("MacBook Air", "Apple", "$1199", "/images/laptops/pexels-junior-teixeira-1064069-2047905.jpg"));
                laptopRepo.save(new Laptop("ThinkPad X1", "Lenovo", "$1299", "/images/laptops/pexels-life-of-pix-7974.jpg"));
                laptopRepo.save(new Laptop("Pavilion 15", "HP", "$849", "/images/laptops/pexels-pixabay-459653.jpg"));
            }
        };
    }

}
