# HTMX Search Implementation Guide

This guide shows how to add real-time laptop search using HTMX to your project.

## Step 1: Update LaptopRepository.java

Replace the content of `src/main/java/com/soul/laptopkade/repository/LaptopRepository.java` with:

```java
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
```

## Step 2: Update HomeController.java

Replace the content of `src/main/java/com/soul/laptopkade/controller/HomeController.java` with:

```java
package com.soul.laptopkade.controller;

import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.LaptopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "home";
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
```

## Step 3: Create search-results.html fragment

Create file `src/main/resources/templates/search-results.html`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div th:fragment="laptop-list">
    <div th:each="laptop : ${laptops}" class="item-card" role="button">
        <img th:src="@{${laptop.imageUrl}}" alt="Laptop Image">
        <div class="item-content">
            <h5 th:text="${laptop.name}">Laptop Name</h5>
            <p class="mb-1" th:text="${laptop.brand}">Brand</p>
        </div>
        <div class="price" th:text="${laptop.price}">$0</div>
    </div>
    <div th:if="${#lists.isEmpty(laptops)}" class="alert alert-info mt-3">
        No laptops found matching your search.
    </div>
</div>
</body>
</html>
```

## Step 4: Update home.html

Replace the content of `src/main/resources/templates/home.html` with:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Laptop Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" th:href="@{/css/style.css}" />
    <script src="https://unpkg.com/htmx.org"></script>
</head>
<body>
<div class="container py-5">
    <div class="header-card">
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-start">
            <div>
                <h1>Welcome to Laptop Store <span class="ribbon">Fresh picks</span></h1>
                <p class="mb-0">Browse curated laptops with images, specs and prices — designed to be simple and friendly.</p>
                <div class="search-row">
                    <input class="form-control" 
                           id="searchInput"
                           type="text" 
                           placeholder="Search laptops, brands..." 
                           aria-label="search"
                           hx-get="/search"
                           hx-trigger="keyup changed delay:500ms, search"
                           hx-target="#laptop-list"
                           hx-swap="innerHTML"
                           hx-include="[name='query']">
                    <button class="btn btn-light" 
                            type="button"
                            hx-get="/search" 
                            hx-target="#laptop-list"
                            hx-include="[name='query']"
                            hx-swap="innerHTML">Search</button>
                    <input type="hidden" name="query" id="queryInput" value="">
                </div>
            </div>
            <div class="mt-3 mt-md-0 text-md-end">
                <small class="d-block">Open now</small>
                <small class="d-block">Free shipping over $500</small>
                <a class="btn btn-outline-light btn-sm mt-2" th:href="@{/laptops/new}">Add Laptop</a>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <div id="laptop-list" class="list-wrapper">
                <div th:each="laptop : ${laptops}" class="item-card" role="button">
                    <img th:src="@{${laptop.imageUrl}}" alt="Laptop Image">
                    <div class="item-content">
                        <h5 th:text="${laptop.name}">Laptop Name</h5>
                        <p class="mb-1" th:text="${laptop.brand}">Brand</p>
                    </div>
                    <div class="price" th:text="${laptop.price}">$0</div>
                </div>
            </div>
            <div class="footer-note">Showing <span id="count" th:text="${#lists.size(laptops)}">0</span> laptops</div>
        </div>
    </div>

</div>

<script>
    // Update hidden query input when user types in search box
    document.getElementById('searchInput').addEventListener('input', function(e) {
        document.getElementById('queryInput').value = e.target.value;
    });
    
    // Update count when HTMX loads new results
    document.body.addEventListener('htmx:afterSwap', function(evt) {
        if (evt.detail.target.id === 'laptop-list') {
            const count = document.querySelectorAll('#laptop-list .item-card').length;
            document.getElementById('count').textContent = count;
        }
    });
</script>
</body>
</html>
```

## How It Works

1. **HTMX Search Box**:
   - `hx-get="/search"` — Sends GET request to /search endpoint
   - `hx-trigger="keyup changed delay:500ms"` — Triggers after user stops typing for 500ms
   - `hx-target="#laptop-list"` — Replaces the laptop list with results
   - `hx-include="[name='query']"` — Includes the search query parameter

2. **Search Endpoint** (`/search`):
   - Returns only the `laptop-list` fragment (not full HTML)
   - Uses custom repository method `searchByNameOrBrand()` for SQL filtering
   - Logs all search operations

3. **JavaScript Updates**:
   - Syncs search input to hidden query field
   - Updates laptop count dynamically after swap

## Build and Run

```cmd
mvnw.cmd clean package -DskipTests
java -jar target\laptopkade-0.0.1-SNAPSHOT.jar
```

Open: http://localhost:8085/home

## Features

✅ Real-time search without page reload
✅ Search by laptop name or brand
✅ Dynamic result count update
✅ Logging for all search operations
✅ Empty state message when no results
✅ 500ms debounce to reduce server load
