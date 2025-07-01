/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.database;

import com.mycompany.ffsmartapplication.models.User;
import com.mycompany.ffsmartapplication.utils.PasswordUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Damola
 */
public class UserDBAccess {
    private static final String INSERT_USER = "INSERT INTO users (username, password, role, authority_level) VALUES (?, ?, ?, ?)";
    private static final String GET_USER_BY_USERNAME = "SELECT user_id, username, password, role, authority_level FROM users WHERE username = ?";
    private static final String GET_ALL_USERS = "SELECT user_id, username, password, role, authority_level FROM users";
    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    
    public static boolean usernameExists(String username) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_USERNAME)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a record is found

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // No match found
    }
    
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getInt("authority_level")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    
    public static String registerUser(User user) {
        // Prevent duplicate username before inserting user
        if (usernameExists(user.getUsername())) {
            return "username_exists";
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {

            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getAuthorityLevel());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0 ? "success" : "error";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error"; // Registration failed
    }
    
    public static User authenticateUser(String username, String password) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_USERNAME)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (PasswordUtils.verifyPassword(password, storedHash)) {
                    return new User(rs.getInt("user_id"), username, storedHash, rs.getString("role"), rs.getInt("authority_level"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }
    
    public static boolean deleteUser(int userId) {
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
        stmt.setInt(1, userId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
