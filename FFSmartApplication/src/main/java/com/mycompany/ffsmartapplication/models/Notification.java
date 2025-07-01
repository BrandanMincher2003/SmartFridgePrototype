/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.models;

/**
 *
 * @author Damola
 */
public class Notification {
    private int id;
    private String itemName;
    private String reason;
    private String message;
    private String timestamp;

    public Notification(int id, String itemName, String reason, String message, String timestamp) {
        this.id = id;
        this.itemName = itemName;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getReason() { return reason; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
}
