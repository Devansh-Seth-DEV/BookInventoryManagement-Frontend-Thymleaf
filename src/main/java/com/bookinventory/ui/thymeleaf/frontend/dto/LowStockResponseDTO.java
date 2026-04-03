package com.bookinventory.ui.thymeleaf.frontend.dto;

public class LowStockResponseDTO {

    private String bookTitle;
    private String isbn;
    private Integer currentStock;

    public LowStockResponseDTO() {}

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
}
