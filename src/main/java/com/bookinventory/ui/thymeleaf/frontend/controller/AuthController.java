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
    
    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    private String getRoleApi() {
        return backendBaseUrl + "/permrole";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        PermRole[] roles =
                restTemplate.getForObject(getRoleApi(), PermRole[].class);

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
            return "redirect:/home";
        }

        // Backend login API
        String url = backendBaseUrl + "/api/auth/login";

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        try {
            UserResponseDTO response =
                    restTemplate.postForObject(url, request, UserResponseDTO.class);

            session.setAttribute("user", response.getUserName());
            session.setAttribute("role", response.getRoleName());
            session.setAttribute("userId", response.getUserId());

            return "redirect:/home";

        } catch(Exception e) {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        PermRole[] roles =
                restTemplate.getForObject(getRoleApi(), PermRole[].class);

        List<PermRole> filtered = Arrays.stream(roles)
                .filter(r -> !r.getPermRole().equalsIgnoreCase("Guest"))
                .filter(r -> !r.getPermRole().equalsIgnoreCase("Admin"))
                .toList();

        model.addAttribute("roles", filtered);
        return "signup";
    }
    
    @PostMapping("/signup")
    public String signup(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String phoneNumber,
                         @RequestParam String userName,
                         @RequestParam String password,
                         @RequestParam Integer roleNumber,
                         HttpSession session) {

        String url = backendBaseUrl + "/api/auth/signup";

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("phoneNumber", phoneNumber);
        user.put("userName", userName);
        user.put("password", password);

        Map<String, Integer> role = new HashMap<>();
        role.put("roleNumber", roleNumber);

        user.put("permRole", role);

        try {
            restTemplate.postForObject(url, user, Object.class);

            session.setAttribute("user", userName);

            if(roleNumber == 2) {
                session.setAttribute("role", "RegisteredUser");
            } else if(roleNumber == 3) {
                session.setAttribute("role", "StoreOwner");
            }

            return "redirect:/home";

        } catch(Exception e) {
            return "redirect:/signup?error=true";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}