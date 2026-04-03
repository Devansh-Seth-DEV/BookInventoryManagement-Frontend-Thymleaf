package com.bookinventory.ui.thymeleaf.frontend.dto;

public class AllBookResponseDTO {

    private String isbn;
    private String title;
    private String description;
    private String categoryName;
    private String publisherName;
    private String edition;

    public AllBookResponseDTO() {}

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
}