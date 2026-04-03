	package com.bookinventory.ui.thymeleaf.frontend.controller;
	
	import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
	
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(HttpSession session) {
        if(session.getAttribute("role") == null) {
            session.setAttribute("role", "Guest");
            session.setAttribute("user", "Guest");
        }
        return "home";
    }
}
