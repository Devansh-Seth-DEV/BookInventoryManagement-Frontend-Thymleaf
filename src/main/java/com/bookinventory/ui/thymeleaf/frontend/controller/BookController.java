package com.bookinventory.ui.thymeleaf.frontend.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.bookinventory.ui.thymeleaf.frontend.dto.AllBookResponseDTO;

@Controller
public class BookController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    @GetMapping("/books")
    public String booksPage(Model model) {

        String url = backendBaseUrl + "/api/books";

        AllBookResponseDTO[] books =
                restTemplate.getForObject(url, AllBookResponseDTO[].class);

        List<AllBookResponseDTO> bookList = Arrays.asList(books);

        model.addAttribute("books", bookList);

        return "books";
    }
}