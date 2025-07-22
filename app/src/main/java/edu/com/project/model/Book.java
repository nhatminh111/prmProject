package edu.com.project.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private String description;
    private String imageUrl;

    public Book(int id, String title, String author, double price, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}
