package com.bookinventory.ui.thymeleaf.frontend.controller;

import com.bookinventory.ui.thymeleaf.frontend.dto.AllBookResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bims.backend.baseurl}")
    private String backendBaseUrl;

    // ─── Guard helper ─────────────────────────────────────────────────────────
    private boolean isAdmin(HttpSession session) {
        return "Admin".equals(session.getAttribute("role"));
    }

    // ─── Show Add Book Form ───────────────────────────────────────────────────
    @GetMapping("/add")
    public String addBookPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/books";
        model.addAttribute("book", new AllBookResponseDTO());
        model.addAttribute("formAction", "/admin/books/add");
        model.addAttribute("formTitle", "Add New Book");
        return "admin-book-form";
    }

    // ─── Submit Add Book ──────────────────────────────────────────────────────
    @PostMapping("/add")
    public String addBook(@RequestParam String isbn,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String categoryName,
                          @RequestParam String publisherName,
                          @RequestParam String edition,
                          HttpSession session,
                          RedirectAttributes ra) {

        if (!isAdmin(session)) return "redirect:/books";

        Map<String, Object> payload = buildPayload(isbn, title, description, categoryName, publisherName, edition);

        try {
            restTemplate.postForObject(backendBaseUrl + "/api/books", payload, Object.class);
            ra.addFlashAttribute("bookSuccess", "\"" + title + "\" added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("bookError", "Failed to add book. Please try again.");
        }
        return "redirect:/books";
    }

    // ─── Show Edit Book Form ──────────────────────────────────────────────────
    @GetMapping("/edit")
    public String editBookPage(@RequestParam String isbn,
                               HttpSession session,
                               Model model) {

        if (!isAdmin(session)) return "redirect:/books";

        try {
            AllBookResponseDTO book =
                    restTemplate.getForObject(backendBaseUrl + "/api/books/" + isbn, AllBookResponseDTO.class);
            model.addAttribute("book", book);
        } catch (Exception e) {
            // fallback: pre-fill only ISBN
            AllBookResponseDTO empty = new AllBookResponseDTO();
            empty.setIsbn(isbn);
            model.addAttribute("book", empty);
        }

        model.addAttribute("formAction", "/admin/books/edit");
        model.addAttribute("formTitle", "Edit Book");
        return "admin-book-form";
    }

    // ─── Submit Edit Book ─────────────────────────────────────────────────────
    @PostMapping("/edit")
    public String editBook(@RequestParam String isbn,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam String categoryName,
                           @RequestParam String publisherName,
                           @RequestParam String edition,
                           HttpSession session,
                           RedirectAttributes ra) {

        if (!isAdmin(session)) return "redirect:/books";

        Map<String, Object> payload = buildPayload(isbn, title, description, categoryName, publisherName, edition);

        try {
            restTemplate.put(backendBaseUrl + "/api/books/" + isbn, payload);
            ra.addFlashAttribute("bookSuccess", "\"" + title + "\" updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("bookError", "Failed to update book. Please try again.");
        }
        return "redirect:/books";
    }

    // ─── Delete Book ──────────────────────────────────────────────────────────
    @PostMapping("/delete")
    public String deleteBook(@RequestParam String isbn,
                             HttpSession session,
                             RedirectAttributes ra) {

        if (!isAdmin(session)) return "redirect:/books";

        try {
            restTemplate.delete(backendBaseUrl + "/api/books/" + isbn);
            ra.addFlashAttribute("bookSuccess", "Book removed successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("bookError", "Failed to remove book. Please try again.");
        }
        return "redirect:/books";
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private Map<String, Object> buildPayload(String isbn, String title, String description,
                                              String categoryName, String publisherName, String edition) {
        Map<String, Object> map = new HashMap<>();
        map.put("isbn", isbn);
        map.put("title", title);
        map.put("description", description);
        map.put("categoryName", categoryName);
        map.put("publisherName", publisherName);
        map.put("edition", edition);
        return map;
    }
}