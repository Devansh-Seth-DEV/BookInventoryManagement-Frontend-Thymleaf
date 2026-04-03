package com.bookinventory.ui.thymeleaf.frontend.controller;

import com.bookinventory.ui.thymeleaf.frontend.dto.AvailableInventoryResponseDTO;
import com.bookinventory.ui.thymeleaf.frontend.dto.InventoryResponseDTO;
import com.bookinventory.ui.thymeleaf.frontend.dto.LowStockResponseDTO;
import com.bookinventory.ui.thymeleaf.frontend.dto.CartItemDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    // ✅ Get all available inventory
    @GetMapping("/inventory/available")
    public String getAvailableInventory(Model model, HttpSession session) {

        AvailableInventoryResponseDTO[] inventory =
                restTemplate.getForObject(
                        backendBaseUrl + "/api/inventory/available",
                        AvailableInventoryResponseDTO[].class
                );

        model.addAttribute("inventory", inventory);

        // Cart count logic (same as BookController)
        @SuppressWarnings("unchecked")
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

        int cartCount = (cart != null)
                ? cart.stream().mapToInt(CartItemDTO::getQuantity).sum()
                : 0;

        model.addAttribute("cartCount", cartCount);

        return "inventory"; // Thymeleaf page name
    }

    // ✅ Get inventory by ID
    @GetMapping("/inventory/{id}")
    public String getInventoryById(@PathVariable("id") Integer id, Model model, HttpSession session) {

        InventoryResponseDTO inventory =
                restTemplate.getForObject(
                        backendBaseUrl + "/api/inventory/" + id,
                        InventoryResponseDTO.class
                );

        model.addAttribute("inventoryItem", inventory);

        // Cart count
        @SuppressWarnings("unchecked")
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

        int cartCount = (cart != null)
                ? cart.stream().mapToInt(CartItemDTO::getQuantity).sum()
                : 0;

        model.addAttribute("cartCount", cartCount);

        return "inventory-details"; // Thymeleaf page
    }

    // ✅ Get low stock items
    @GetMapping("/inventory/low-stock")
    public String getLowStock(Model model, HttpSession session) {

        LowStockResponseDTO[] lowStock =
                restTemplate.getForObject(
                        backendBaseUrl + "/api/inventory/low-stock",
                        LowStockResponseDTO[].class
                );

        model.addAttribute("lowStock", lowStock);

        return "low-stock"; // Thymeleaf page
    }
}
