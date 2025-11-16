# Laptop Store API - MySQL Quick Setup

## Quick Start (5 Steps)

### Step 1: Create MySQL Database
Open MySQL Command Line or Workbench and run:
```sql
CREATE DATABASE IF NOT EXISTS laptopkade;
USE laptopkade;

CREATE TABLE IF NOT EXISTS laptops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    processor VARCHAR(100) NOT NULL,
    ram VARCHAR(50) NOT NULL,
    storage VARCHAR(100) NOT NULL,
    graphics_card VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_brand ON laptops(brand);
CREATE INDEX idx_model ON laptops(model);
CREATE INDEX idx_price ON laptops(price);
CREATE INDEX idx_stock ON laptops(stock);
```

### Step 2: Verify MySQL Connection Details
Update `src/main/resources/application.properties` if needed:
```properties
spring.datasource.username=root          # Your MySQL username
spring.datasource.password=              # Your MySQL password (if any)
spring.datasource.url=jdbc:mysql://localhost:3306/laptopkade
```

### Step 3: Build Project
```bash
cd d:\springboot_project\Laptop-store\laptopkade_api
mvn clean install -DskipTests
```

### Step 4: Run Application
```bash
mvn spring-boot:run
```

### Step 5: Test API
```bash
curl http://localhost:8085/api/laptops/health
```

---

## Test Sample API Calls

### Create a Laptop
```bash
curl -X POST http://localhost:8085/api/laptops \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Dell",
    "model": "XPS 13",
    "processor": "Intel i7",
    "ram": "16GB",
    "storage": "512GB SSD",
    "graphicsCard": "Intel Iris Xe",
    "price": 1299.99,
    "stock": 10,
    "description": "Premium ultrabook"
  }'
```

### Get All Laptops
```bash
curl http://localhost:8085/api/laptops
```

### Get Statistics
```bash
curl http://localhost:8085/api/laptops/stats/total-count
curl http://localhost:8085/api/laptops/stats/average-price
```

---

## Important Files
- `application.properties` - Database configuration
- `database-setup.sql` - SQL script for database setup
- `MYSQL_SETUP_GUIDE.md` - Detailed setup guide
- `pom.xml` - Maven dependencies (MySQL driver included)

---

## Troubleshooting

### MySQL not connecting?
1. Ensure MySQL service is running
2. Check username/password in application.properties
3. Verify database `laptopkade` exists
4. Check MySQL is on port 3306

### Build fails?
Run: `mvn clean install -DskipTests`

### Port already in use?
Change port in `application.properties`:
```properties
server.port=8086
```

---

## Database Schema
- **id**: Auto-increment primary key
- **brand**: Laptop manufacturer
- **model**: Model name
- **processor**: CPU details
- **ram**: Memory size
- **storage**: Storage type and size
- **graphics_card**: GPU details
- **price**: Laptop price (Double)
- **stock**: Available quantity (Integer)
- **description**: Product description (up to 1000 chars)
- **created_at**: Auto timestamp
- **updated_at**: Auto update timestamp

---

## API Endpoints Available

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/laptops` | Create laptop |
| GET | `/api/laptops` | Get all laptops |
| GET | `/api/laptops/{id}` | Get by ID |
| GET | `/api/laptops/brand/{brand}` | Filter by brand |
| GET | `/api/laptops/search?keyword=...` | Search |
| GET | `/api/laptops/in-stock` | In stock items |
| PUT | `/api/laptops/{id}` | Update laptop |
| PATCH | `/api/laptops/{id}/price?price=...` | Update price |
| DELETE | `/api/laptops/{id}` | Delete laptop |
| GET | `/api/laptops/stats/...` | Get statistics |

All endpoints documented in CURL_COMMANDS.md
