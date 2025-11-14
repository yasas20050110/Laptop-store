package com.soul.laptopkade.controller;

import com.soul.laptopkade.model.CartItem;
import com.soul.laptopkade.model.Laptop;
import com.soul.laptopkade.repository.LaptopRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CartController {

    private final LaptopRepository laptopRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    public CartController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Object o = session.getAttribute("cart");
        if (o == null) {
            Map<Long, CartItem> cart = new HashMap<>();
            session.setAttribute("cart", cart);
            return cart;
        }
        return (Map<Long, CartItem>) o;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("laptopId") Long laptopId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Optional<Laptop> opt = laptopRepository.findById(laptopId);
        if (opt.isEmpty()) {
            logger.warn("ADD_TO_CART failed: Laptop id {} not found", laptopId);
            return "redirect:/home";
        }
        Laptop laptop = opt.get();
        Map<Long, CartItem> cart = getCart(session);
        CartItem item = cart.get(laptopId);
        if (item == null) {
            item = new CartItem(laptop.getId(), laptop.getName(), laptop.getBrand(), laptop.getPrice(), laptop.getImageUrl(), quantity);
            cart.put(laptopId, item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        session.setAttribute("cart", cart);
        logger.info("CART operation: Added laptop id {} qty {} to cart (sessionId={})", laptopId, quantity, session.getId());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("laptopId") Long laptopId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Long, CartItem> cart = getCart(session);
            if (cart.containsKey(laptopId)) {
                cart.remove(laptopId);
                session.setAttribute("cart", cart);
                logger.info("CART operation: Removed laptop id {} from cart (sessionId={})", laptopId, session.getId());
            }
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/cart");
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        double total = cart.values().stream().mapToDouble(CartItem::getTotal).sum();
        model.addAttribute("items", cart.values());
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        // In real app: process payment, create order, etc. Here we simulate purchase and clear cart
        double total = cart.values().stream().mapToDouble(CartItem::getTotal).sum();
        logger.info("CHECKOUT operation: Purchased {} items totaling ${} (sessionId={})", cart.size(), total, session.getId());
        cart.clear();
        session.setAttribute("cart", cart);
        return "redirect:/home";
    }
}
