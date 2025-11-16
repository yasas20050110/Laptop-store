# MySQL Database Setup Guide for Laptop Store API

## Prerequisites

Before running the Spring Boot application with MySQL, ensure you have:
- MySQL Server installed and running (version 5.7 or higher)
- MySQL Client or MySQL Workbench
- Java 21 installed
- Maven installed

---

## Step 1: Install MySQL

### Windows
1. Download MySQL from: https://dev.mysql.com/downloads/mysql/
2. Run the installer and follow the setup wizard
3. During installation, set:
   - MySQL Server Port: **3306** (default)
   - MySQL Root Password: Set a password or leave blank
4. Start MySQL Service

### macOS
```bash
brew install mysql
brew services start mysql
```

### Linux (Ubuntu/Debian)
```bash
sudo apt-get install mysql-server
sudo mysql_secure_installation
sudo systemctl start mysql
```

---

## Step 2: Create Database and Tables

### Option A: Using MySQL Command Line

1. Open Command Prompt/Terminal
2. Connect to MySQL:
```bash
mysql -u root -p
```
(Enter your MySQL password when prompted)

3. Copy and paste the SQL script from `database-setup.sql`:
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_laptop (brand, model)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_brand ON laptops(brand);
CREATE INDEX idx_model ON laptops(model);
CREATE INDEX idx_price ON laptops(price);
CREATE INDEX idx_stock ON laptops(stock);
```

4. Exit MySQL:
```sql
EXIT;
```

### Option B: Using MySQL Workbench

1. Open MySQL Workbench
2. Create a new connection if needed
3. Open a new SQL query tab
4. Copy and paste the SQL script from `database-setup.sql`
5. Execute the script (Ctrl + Enter or click Execute)

### Option C: Using Command Line

```bash
mysql -u root -p < database-setup.sql
```

---

## Step 3: Verify Database Creation

```bash
mysql -u root -p
```

Then run:
```sql
SHOW DATABASES;
USE laptopkade;
SHOW TABLES;
SELECT * FROM laptops;
```

---

## Step 4: Update Spring Boot Configuration

The `application.properties` file is already configured for MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/laptopkade?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

### Modify if needed:
- `spring.datasource.username` - Your MySQL username (default: root)
- `spring.datasource.password` - Your MySQL password (leave empty if no password)
- `spring.datasource.url` - MySQL server address and database name

---

## Step 5: Build and Run the Application

1. Navigate to project directory:
```bash
cd d:\springboot_project\Laptop-store\laptopkade_api
```

2. Build the project:
```bash
mvn clean install -DskipTests
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

---

## Step 6: Verify the Connection

Once the application starts, you should see logs like:
```
HikariPool-1 - Starting...
HikariPool-1 - Added connection conn0: url=jdbc:mysql://localhost:3306/laptopkade
HikariPool-1 - Start completed.
```

Test the API with:
```bash
curl http://localhost:8085/api/laptops/health
```

Expected response: `"Laptop API is running"`

---

## Troubleshooting

### Error: "Access denied for user 'root'@'localhost'"
**Solution**: Update the password in `application.properties`
```properties
spring.datasource.password=your_mysql_password
```

### Error: "Unknown database 'laptopkade'"
**Solution**: Run the database setup script again to create the database

### Error: "Can't connect to MySQL server on 'localhost:3306'"
**Solution**: 
- Ensure MySQL service is running:
  - Windows: Check Services (services.msc)
  - macOS: `brew services list`
  - Linux: `sudo systemctl status mysql`

### Error: "Communications link failure"
**Solution**: 
- Check MySQL connection parameters in `application.properties`
- Verify MySQL port is 3306 (default)
- Ensure MySQL Server is running

### Slow Queries
**Solution**: The indexes have been created automatically in the `database-setup.sql` script

---

## Common MySQL Commands

```bash
# Connect to MySQL
mysql -u root -p

# Show all databases
SHOW DATABASES;

# Use a specific database
USE laptopkade;

# Show all tables
SHOW TABLES;

# Describe table structure
DESCRIBE laptops;

# View all data
SELECT * FROM laptops;

# View data count
SELECT COUNT(*) FROM laptops;

# Exit MySQL
EXIT;
```

---

## Switching Back to H2 (In-Memory Database)

If you want to temporarily use H2 instead of MySQL, comment out MySQL config and uncomment H2 config in `application.properties`:

```properties
# Comment out MySQL
# spring.datasource.url=jdbc:mysql://localhost:3306/laptopkade...

# Uncomment H2
spring.datasource.url=jdbc:h2:mem:laptopkadedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## Production Considerations

For production deployment, consider:
1. Use a strong password for MySQL user
2. Set `spring.jpa.hibernate.ddl-auto=validate` (don't auto-update schema)
3. Use connection pooling (already configured with HikariCP)
4. Enable SSL for database connections
5. Set up regular backups
6. Monitor database performance

---

## API Base URL
```
http://localhost:8085/api/laptops
```

All API endpoints are ready to use!
