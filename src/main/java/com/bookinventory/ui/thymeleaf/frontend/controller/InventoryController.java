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

    @GetMapping("/inventory")
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
