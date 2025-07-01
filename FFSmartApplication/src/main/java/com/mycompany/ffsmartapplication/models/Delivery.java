package com.mycompany.ffsmartapplication.models;

public class Delivery {
    private String supplierName;
    private String itemName;
    private int quantity;
    
        // Constructors
    public Delivery() {
    }

    public Delivery(String supplierName, String itemName, int quantity) {
        this.supplierName = supplierName;
        this.itemName = itemName;
        this.quantity = quantity;
    }
    // getters and setters
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
