package com.bookinventory.ui.thymeleaf.frontend.controller;

import com.bookinventory.ui.thymeleaf.frontend.dto.CartItemDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    // ─── View Cart ────────────────────────────────────────────────────────────
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {

        // Only RegisteredUser can access cart
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("RegisteredUser")) {
            return "redirect:/login";
        }

        List<CartItemDTO> cart = getCart(session);
        model.addAttribute("cartItems", cart);
        model.addAttribute("totalItems", cart.stream().mapToInt(CartItemDTO::getQuantity).sum());
        return "cart";
    }

    // ─── Add to Cart ──────────────────────────────────────────────────────────
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String isbn,
                            @RequestParam String title,
                            @RequestParam String description,
                            @RequestParam String categoryName,
                            @RequestParam String publisherName,
                            @RequestParam String edition,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("RegisteredUser")) {
            redirectAttributes.addFlashAttribute("cartError", "Please login as a Registered User to add items to cart.");
            return "redirect:/books";
        }

        List<CartItemDTO> cart = getCart(session);

        // Check if book already in cart → increment quantity
        boolean found = false;
        for (CartItemDTO item : cart) {
            if (item.getIsbn().equals(isbn)) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItemDTO(isbn, title, description, categoryName, publisherName, edition));
        }

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("cartSuccess", "\"" + title + "\" added to cart!");
        return "redirect:/books";
    }

    // ─── Remove from Cart ─────────────────────────────────────────────────────
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam String isbn,
                                 HttpSession session) {

        List<CartItemDTO> cart = getCart(session);
        cart.removeIf(item -> item.getIsbn().equals(isbn));
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    // ─── Update Quantity ──────────────────────────────────────────────────────
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam String isbn,
                                 @RequestParam int quantity,
                                 HttpSession session) {

        List<CartItemDTO> cart = getCart(session);
        if (quantity <= 0) {
            cart.removeIf(item -> item.getIsbn().equals(isbn));
        } else {
            for (CartItemDTO item : cart) {
                if (item.getIsbn().equals(isbn)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    // ─── Clear Cart ───────────────────────────────────────────────────────────
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<CartItemDTO>());
        return "redirect:/cart";
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private List<CartItemDTO> getCart(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}