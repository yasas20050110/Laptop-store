# Laptop Store - CRUD Logging Setup

## Overview
This project uses **SLF4J with Logback** for logging CRUD (Create, Read, Update, Delete) operations. Logs are written to both console and files.

## Log Files Location
- **Main logs**: `logs/laptopkade.log` — All application logs
- **CRUD logs**: `logs/crud-operations.log` — Detailed CRUD operation logs

## Log Files Details

### logs/laptopkade.log
- Contains all application logs at INFO level and above
- Rolls over daily or when size exceeds 10MB
- Keeps up to 10 days of history

### logs/crud-operations.log
- Contains only controller/CRUD operation logs
- Same rolling policy as main logs
- Useful for tracking user actions and data modifications

## Log Format
```
2025-11-12 17:35:42.123 [main] INFO com.soul.laptopkade.controller.HomeController - READ operation: Fetching all laptops from database
2025-11-12 17:35:42.145 [main] INFO com.soul.laptopkade.controller.HomeController - READ operation successful: Retrieved 4 laptops
2025-11-12 17:35:55.230 [main] INFO com.soul.laptopkade.controller.LaptopController - CREATE operation started: Creating new laptop - Name: My Laptop, Brand: Acme, Price: $499
2025-11-12 17:35:55.456 [main] INFO com.soul.laptopkade.controller.LaptopController - CREATE operation successful: Laptop saved with ID: 5
```

## CRUD Operations Logged

### CREATE (Add Laptop)
- **Endpoint**: `POST /laptops`
- **Logged**: Laptop details (name, brand, price) and file upload status
- **Example Log**:
  ```
  CREATE operation started: Creating new laptop - Name: XPS 13, Brand: Dell, Price: $999
  File upload successful: 1699804554456_image.jpg
  CREATE operation successful: Laptop saved with ID: 5
  ```

### READ (View Laptops)
- **Endpoint**: `GET /home` or `GET /`
- **Logged**: Number of laptops fetched
- **Example Log**:
  ```
  READ operation: Fetching all laptops from database
  READ operation successful: Retrieved 4 laptops
  ```

### UPDATE (Edit Laptop)
- Not yet implemented in this version
- When added, will log: laptop ID, updated fields, and success/failure

### DELETE (Remove Laptop)
- Not yet implemented in this version
- When added, will log: laptop ID and deletion status

## Configuration Files

### logback.xml
Located at: `src/main/resources/logback.xml`

Configures:
- **Console appender**: Logs to console during development
- **File appender**: Writes to `logs/laptopkade.log`
- **CRUD appender**: Writes controller logs to `logs/crud-operations.log`
- **Rolling policy**: Daily rollover + 10MB size trigger

### application.properties
Logging levels can be adjusted via:
```properties
logging.level.com.soul.laptopkade.controller=DEBUG
logging.level.org.springframework.web=DEBUG
```

## How to View Logs

### During Development (Running in IDE)
- Console output shows logs in real-time
- Check console tab while app is running

### Production (Running JAR)
- Main logs: `logs/laptopkade.log`
- CRUD logs: `logs/crud-operations.log`
- Example:
  ```cmd
  tail -f logs/crud-operations.log
  ```

### View All Logs at Once
```cmd
cat logs/laptopkade.log
```

## Common Log Patterns

**Successful CREATE operation**:
```
CREATE operation started: Creating new laptop - Name: MacBook Air, Brand: Apple, Price: $1199
CREATE operation successful: Laptop saved with ID: 6
```

**Failed operation with exception**:
```
CREATE operation failed: Error while creating laptop
java.sql.SQLException: Database connection error
```

**READ with zero laptops**:
```
READ operation: Fetching all laptops from database
READ operation successful: Retrieved 0 laptops
```

## Tips for Log Analysis

1. **Find all CREATE operations**: `grep "CREATE operation" logs/crud-operations.log`
2. **Find errors**: `grep "ERROR" logs/laptopkade.log`
3. **Find operations for a specific laptop**: `grep "Name: XPS 13" logs/crud-operations.log`
4. **Real-time log monitoring** (on Windows in another terminal):
   ```cmd
   type logs\crud-operations.log
   ```

## Future Enhancements

- [ ] Add UPDATE operation logging
- [ ] Add DELETE operation logging
- [ ] Add User/IP tracking in logs
- [ ] Implement log filtering UI in admin panel
- [ ] Metrics collection (operation duration, success rates)

