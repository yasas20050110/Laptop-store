# 🔐 Admin Login Quick Start Guide

## Overview
Your Laptop Store now has a **secure admin authentication system**. Only authenticated admins can add new laptops to the database.

---

## 🚀 Quick Start

### 1. **Start the Application**
```cmd
cd d:\springboot_project\Laptop-Store\laptopkade
java -jar target\laptopkade-0.0.1-SNAPSHOT.jar
```

### 2. **Open Browser**
Navigate to: **http://localhost:8085/home**

### 3. **Login as Admin**
- Click **"🔐 Admin Login"** button (top-right)
- Enter credentials:
  - **Username:** `admin`
  - **Password:** `admin123`
- Click **"Sign In"**

### 4. **Add Laptops**
- Click **"+ Add Laptop"** button
- Fill in laptop details
- Upload an image
- Click **"Save Laptop"**

### 5. **Logout**
- Click **"Logout"** button when done

---

## 🔓 Features

### **For Public Users:**
- ✅ Browse all laptops
- ✅ Search laptops by name or brand (real-time HTMX)
- ✅ View laptop details
- ❌ Cannot add laptops (needs admin login)

### **For Admin Users:**
- ✅ All public features
- ✅ Add new laptops
- ✅ Upload laptop images
- ✅ See admin controls

---

## 📝 Demo Accounts

| Username | Password | Role | Status |
|----------|----------|------|--------|
| `admin` | `admin123` | ADMIN | ✅ Pre-seeded |

---

## 🔒 Security Features

- **Password Hashing:** BCrypt encryption (secure)
- **Session Management:** Automatic session tracking
- **Role-based Access:** ROLE_ADMIN required for adding laptops
- **CSRF Protection:** Configured in SecurityConfig
- **Logout:** Clears session and redirects to home

---

## 📊 Files Changed

### **New Files:**
- `src/main/java/.../model/Admin.java` - Admin entity
- `src/main/java/.../repository/AdminRepository.java` - Admin repository
- `src/main/java/.../controller/AuthController.java` - Login controller
- `src/main/java/.../config/SecurityConfig.java` - Spring Security config
- `src/main/resources/templates/login.html` - Login page

### **Modified Files:**
- `pom.xml` - Added Spring Security dependency
- `src/main/java/.../DataLoader.java` - Seed admin user
- `src/main/resources/templates/home.html` - Dynamic login/logout button
- `src/main/resources/templates/add.html` - Added logout option

---

## 🧪 Test Scenarios

### **Scenario 1: Try Adding Without Login**
1. On home page, click "🔐 Admin Login"
2. ✅ Redirected to login page
3. Go back to home, button still shows "Admin Login" ✅

### **Scenario 2: Login & Add Laptop**
1. Click "🔐 Admin Login"
2. Enter `admin` / `admin123`
3. ✅ Redirected to Add Laptop form
4. Fill in details and save
5. ✅ Laptop appears in home page list

### **Scenario 3: Logout**
1. From add page, click "Logout"
2. ✅ Redirected to home page
3. ✅ Button changes to "🔐 Admin Login"

### **Scenario 4: Search Still Works**
1. On home page, type in search box
2. ✅ Real-time results update (HTMX)
3. Works whether logged in or not

---

## 🛠️ Troubleshooting

### **Can't Login?**
- Check credentials: `admin` / `admin123`
- Make sure application is running on port 8085
- Check browser console for errors (F12)

### **Add Button Not Showing?**
- You need to be logged in as admin
- Try logging out and logging back in
- Clear browser cache (Ctrl+Shift+Del)

### **Logout Not Working?**
- Click the Logout button again
- Try clearing cookies manually
- Refresh the page

### **Database Reset?**
- Delete `target` folder
- Rebuild: `mvnw.cmd clean package`
- DataLoader will re-seed admin and laptops

---

## 📝 Application.properties

Location: `src/main/resources/application.properties`

Current settings:
```properties
spring.application.name=laptopkade
server.port=8085

# Database: H2 in-memory (development)
spring.datasource.url=jdbc:h2:mem:laptopdb
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🎨 UI Styling

- **Theme:** Purple (#7c3aed) to Teal (#0ea5a4) gradient
- **Responsive:** Works on mobile, tablet, desktop
- **Dark mode:** Light background with contrasting cards

---

## 📈 What's Next?

- [ ] Add more admin accounts (admin panel)
- [ ] Password reset functionality
- [ ] Edit/Delete laptop capabilities
- [ ] Admin dashboard
- [ ] Audit logging

---

## ✨ Summary

Your laptop store now has:
- ✅ Secure admin authentication
- ✅ Database-backed user storage
- ✅ Role-based access control
- ✅ Beautiful login page
- ✅ Session management
- ✅ Public browsing & HTMX search
- ✅ Admin-only add laptop feature

**Ready to use!** 🚀

---

For detailed documentation, see: `AUTHENTICATION.md`
