package com.bookinventory.ui.thymeleaf.frontend.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.bookinventory.ui.thymeleaf.frontend.dto.UserResponseDTO;
import com.bookinventory.ui.thymeleaf.frontend.model.PermRole;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${bims.backend.baseurl}"+"/permrole")
    private String ROLE_API;

    @GetMapping("/login")
    public String loginPage(Model model) {
        PermRole[] roles = restTemplate.getForObject(ROLE_API, PermRole[].class);
        model.addAttribute("roles", roles);
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam Integer roleNumber,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password,
                        HttpSession session) {

        // Guest login
        if(roleNumber == 1) {
            session.setAttribute("user", "Guest");
            session.setAttribute("role", "Guest");
            return "redirect:/";
        }

        // Call backend login API
        String url = "http://localhost:8085/api/users/login";

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        try {
            UserResponseDTO response = restTemplate.postForObject(url, request, UserResponseDTO.class);

            session.setAttribute("user", response.getUserName());
            session.setAttribute("role", response.getRoleName());
//            session.setAttribute("userId", response.getUserId());

            return "redirect:/";

        } catch(Exception e) {
            return "login?error=true";
        }
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        PermRole[] roles = restTemplate.getForObject(ROLE_API, PermRole[].class);

        List<PermRole> filtered = Arrays.stream(roles)
                .filter(r -> !r.getPermRole().equalsIgnoreCase("Guest"))
                .filter(r -> !r.getPermRole().equalsIgnoreCase("Admin"))
                .toList();

        model.addAttribute("roles", filtered);
        return "signup";
    }
    
    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam Integer roleNumber,
                        HttpSession session) {

        session.setAttribute("user", username);
        session.setAttribute("role", roleNumber);

        return "redirect:/";
    }
}