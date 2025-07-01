/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.models;

/**
 *
 * @author user
 */
public class User {
    private final int id; // ID is final since it will be assigned in the DB
    private String username;
    private String password;
    private String role;
    private int authorityLevel; // Authority level based on role

    // Constructor for login
    public User(int id, String username, String password, String role, int authorityLevel) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorityLevel = authorityLevel;
    }

    // Constructor for registration
    public User(String username, String password, String role) {
        this.id = -1; // Placeholder as it will be auto assigned when added to table
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorityLevel = assignAuthorityLevel(role);
    }

    private int assignAuthorityLevel(String role) {
        switch (role) {
            case "Admin":
                return 4;
            case "Head Chef":
                return 3;
            case "Chef":
                return 2;
            case "Delivery Person":
                return 1;
            default:
                return 1; // Default to lowest level
        }
    }

    public int getId() { return id; }
    
    public String getUsername() { return username; }
    
    public String getPassword() { return password; }
    
    public String getRole() { return role; }
    
    public int getAuthorityLevel() { return authorityLevel; }
}
