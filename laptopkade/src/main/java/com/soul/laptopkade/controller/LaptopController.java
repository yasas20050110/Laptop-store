package com.soul.laptopkade.controller;

import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.LaptopRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Controller
public class LaptopController {

    private final LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping("/laptops/new")
    public String showCreateForm(Model model) {
        model.addAttribute("laptop", new Laptop());
        return "add";
    }

    @PostMapping("/laptops")
    public String createLaptop(@ModelAttribute Laptop laptop,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        // handle file upload (optional)
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // save under project static images folder for development
                String uploadsDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "laptops" + File.separator;
                File uploadDir = new File(uploadsDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String original = Path.of(imageFile.getOriginalFilename()).getFileName().toString();
                String filename = System.currentTimeMillis() + "_" + original;
                File dest = new File(uploadDir, filename);
                imageFile.transferTo(dest);
                laptop.setImageUrl("/images/laptops/" + filename);
            } catch (IOException e) {
                // log and continue without image
                e.printStackTrace();
            }
        }

        laptopRepository.save(laptop);
        return "redirect:/home";
    }

}
