package com.bookinventory.ui.thymeleaf.frontend.dto;

public class CartItemDTO {

    private String isbn;
    private String title;
    private String description;
    private String categoryName;
    private String publisherName;
    private String edition;
    private int quantity;

    public CartItemDTO() {}

    public CartItemDTO(String isbn, String title, String description,
                       String categoryName, String publisherName, String edition) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
        this.publisherName = publisherName;
        this.edition = edition;
        this.quantity = 1;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}