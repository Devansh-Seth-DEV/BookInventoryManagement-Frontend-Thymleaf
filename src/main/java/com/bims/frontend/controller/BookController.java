package com.bims.frontend.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class BookController {

	@GetMapping("/books")
    public String home() {
        return "books";
    }
	
}
