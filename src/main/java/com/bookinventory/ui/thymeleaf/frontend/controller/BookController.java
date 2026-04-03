package com.bookinventory.ui.thymeleaf.frontend.controller;

import com.bookinventory.ui.thymeleaf.frontend.dto.AllBookResponseDTO;
import com.bookinventory.ui.thymeleaf.frontend.dto.CartItemDTO;
import jakarta.servlet.http.HttpSession;
<<<<<<< fix/add-to-cart
=======
import java.util.Arrays;
import java.util.List;

>>>>>>> develop
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
<<<<<<< fix/add-to-cart

import java.util.List;
=======

import java.util.List;


import com.bookinventory.ui.thymeleaf.frontend.dto.AllBookResponseDTO;
>>>>>>> develop

@Controller
public class BookController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    @GetMapping("/books")
    public String books(Model model, HttpSession session) {

        AllBookResponseDTO[] books =
                restTemplate.getForObject(backendBaseUrl + "/api/books", AllBookResponseDTO[].class);

        model.addAttribute("books", books);

        // Pass cart count to show badge on navbar
        @SuppressWarnings("unchecked")
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        int cartCount = (cart != null) ? cart.stream().mapToInt(CartItemDTO::getQuantity).sum() : 0;
        model.addAttribute("cartCount", cartCount);

        return "books";
    }
}