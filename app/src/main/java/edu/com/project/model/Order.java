package edu.com.project.model;

import java.util.List;

public class Order {
    private int id;
    private double total;
    private String date;
    private List<OrderDetail> details;

    public Order(int id, double total, String date, List<OrderDetail> details) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.details = details;
    }

    public int getId() { return id; }
    public double getTotal() { return total; }
    public String getDate() { return date; }
    public List<OrderDetail> getDetails() { return details; }
}

