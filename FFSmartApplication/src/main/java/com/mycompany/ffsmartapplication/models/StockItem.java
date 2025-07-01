package com.mycompany.ffsmartapplication.models;

import java.time.LocalDate;

public class StockItem {
    private final int itemId; // ID is final since it will be assigned in the DB
    private String name;
    private int quantity;
    private LocalDate expiryDate;

    // Constructor for reading from db
    public StockItem(int itemId, String name, int quantity, LocalDate expiryDate) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    // Constructor for writing to db
    public StockItem(String name, int quantity, LocalDate expiryDate) {
        this.itemId = -1; // Placeholder
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public int getItemId() { return itemId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}