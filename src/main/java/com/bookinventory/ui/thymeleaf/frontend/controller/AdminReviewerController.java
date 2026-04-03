package com.bookinventory.ui.thymeleaf.frontend.controller;

import com.bookinventory.ui.thymeleaf.frontend.dto.AllBookReviewerResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/reviewers")
public class AdminReviewerController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    private boolean isAdmin(HttpSession session) {
        return "Admin".equals(session.getAttribute("role"));
    }

    @GetMapping
    public String listReviewers(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        try {
            AllBookReviewerResponseDTO[] reviewers = restTemplate.getForObject(
                    backendBaseUrl + "/api/reviewers",
                    AllBookReviewerResponseDTO[].class
            );

            List<AllBookReviewerResponseDTO> reviewerList =
                    reviewers != null ? Arrays.asList(reviewers) : Collections.emptyList();

            model.addAttribute("reviewers", reviewerList);

        } catch (Exception e) {
            model.addAttribute("reviewers", Collections.emptyList());
            model.addAttribute("reviewerError", "Failed to load reviewers.");
            e.printStackTrace();
        }

        return "admin-reviewer-list";
    }
}