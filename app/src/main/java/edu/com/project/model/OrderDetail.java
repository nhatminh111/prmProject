package edu.com.project.model;

public class OrderDetail {
    private String bookTitle;
    private int quantity;
    private double price;

    public OrderDetail(String bookTitle, int quantity, double price) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }

    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
