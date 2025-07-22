package edu.com.project.model;

public class CartItem {
    private int id;
    private String title;
    private String author;
    private double price;
    private int quantity;
    private String imageUrl;

    public CartItem(int id, String title, String author, double price, int quantity, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public CartItem(int id, String title, String author, double price, int quantity) {
        this(id, title, author, price, quantity, null);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }

    public double getTotal() { return price * quantity; }
}