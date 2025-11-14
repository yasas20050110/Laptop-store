package com.soul.laptopkade.controller;

import com.soul.laptopkade.model.User;
import com.soul.laptopkade.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam(value = "phone", defaultValue = "") String phone,
                               Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        User user = new User(username, email, passwordEncoder.encode(password), phone, "", "", "");
        userRepository.save(user);
        logger.info("USER_REGISTRATION: New user registered: username='{}', email='{}'", username, email);
        return "redirect:/user-login";
    }

    @GetMapping("/user-login")
    public String userLoginPage() {
        return "user-login";
    }

    @PostMapping("/user-authenticate")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpSession session,
                                   Model model) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            return "user-login";
        }
        User user = userOpt.get();
        session.setAttribute("loggedInUser", user);
        logger.info("USER_LOGIN: User '{}' logged in successfully", username);
        return "redirect:/home";
    }

    @GetMapping("/user-logout")
    public String userLogout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            logger.info("USER_LOGOUT: User '{}' logged out", user.getUsername());
        }
        session.removeAttribute("loggedInUser");
        return "redirect:/home";
    }
}

