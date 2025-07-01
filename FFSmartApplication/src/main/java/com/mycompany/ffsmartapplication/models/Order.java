/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.models;

/**
 *
 * @author Damola
 */
public class Order {
    private int orderNumber;
    private String itemName;
    private int quantity;
    private String supplier;

    // Constructor for writing to db
    public Order(String itemName, int quantity, String supplier) {
        this.orderNumber = -1;
        this.itemName = itemName;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    // Constructor for reading from db
    public Order(int orderNumber, String itemName, int quantity, String supplier) {
        this.orderNumber = orderNumber;
        this.itemName = itemName;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    // Getters and setters
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public String getQuantityString() {
        return String.valueOf(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
}
