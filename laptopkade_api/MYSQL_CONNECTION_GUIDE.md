# ✅ MySQL Database Setup Instructions

Follow these steps carefully to connect your Laptop Store API to MySQL.

---

## 📋 Prerequisites

- MySQL Server 5.7 or higher
- MySQL running on port 3306 (default)
- MySQL Command Line or Workbench

---

## 🚀 QUICK SETUP (2 Minutes)

### Step 1: Open MySQL Command Line
```bash
mysql -u root -p
# Enter your MySQL root password
```

### Step 2: Create Database and Table (Copy & Paste)
```sql
CREATE DATABASE IF NOT EXISTS laptopkade;
USE laptopkade;

CREATE TABLE laptops (
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

### Step 3: Exit MySQL
```sql
EXIT;
```

### Step 4: Update Configuration (if needed)
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root          # Your MySQL username
spring.datasource.password=              # Your MySQL password (if any)
```

### Step 5: Run Application
```bash
cd d:\springboot_project\Laptop-store\laptopkade_api
mvn spring-boot:run
```

---

## ✅ Verification

Once running, you should see:
```
HikariPool-1 - Added connection conn0: url=jdbc:mysql://localhost:3306/laptopkade
```

Test the API:
```bash
curl http://localhost:8085/api/laptops/health
```

Response: `"Laptop API is running"`

---

## 🔧 Alternative: Use H2 (In-Memory) for Testing

If MySQL is not ready yet, you can test with H2:

**Option A**: Run with H2 profile
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"
```

**Option B**: Edit `application.properties` - Uncomment H2 section and comment MySQL section

---

## 🆘 Troubleshooting

### ❌ "Communications link failure"
**Fix**: MySQL is not running or database doesn't exist
1. Check MySQL is running: `mysql -u root -p` (should connect)
2. Run the SQL script above to create the database
3. Verify database exists: `SHOW DATABASES;` (should show `laptopkade`)

### ❌ "Access denied for user 'root'@'localhost'"
**Fix**: Wrong password
1. Update `spring.datasource.password=` in `application.properties`
2. Set correct MySQL password

### ❌ "Unknown database 'laptopkade'"
**Fix**: Database not created
1. Run the SQL script above to create the database
2. Verify with: `USE laptopkade;` and `SHOW TABLES;`

### ❌ "Port 8085 already in use"
**Fix**: Change port in `application.properties`
```properties
server.port=8086
```

---

## 📊 Database Schema

```sql
SELECT * FROM laptops;

-- Columns:
-- id (AUTO_INCREMENT)
-- brand (VARCHAR 100)
-- model (VARCHAR 100)
-- processor (VARCHAR 100)
-- ram (VARCHAR 50)
-- storage (VARCHAR 100)
-- graphics_card (VARCHAR 100)
-- price (DOUBLE)
-- stock (INT)
-- description (VARCHAR 1000)
-- created_at (TIMESTAMP)
-- updated_at (TIMESTAMP)
```

---

## 🧪 Test API

### Create Laptop
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

## 📁 Important Files

- `application.properties` - Main config (MySQL enabled by default)
- `application-h2.properties` - H2 config (for testing)
- `database-setup.sql` - SQL script for manual setup
- `MYSQL_SETUP_GUIDE.md` - Detailed guide
- `MYSQL_QUICK_START.md` - Quick reference

---

## ⚙️ Configuration Details

### MySQL Connection
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/laptopkade
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

### Connection Pool
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
```

---

## 🎯 Next Steps

1. ✅ Create database using SQL script above
2. ✅ Start MySQL service
3. ✅ Build project: `mvn clean install`
4. ✅ Run application: `mvn spring-boot:run`
5. ✅ Test API endpoints

---

**You're all set!** The Laptop Store API is ready to use with MySQL. 🚀
