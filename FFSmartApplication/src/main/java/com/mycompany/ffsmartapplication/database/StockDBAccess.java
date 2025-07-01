/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.database;

import com.mycompany.ffsmartapplication.models.StockItem;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Damola
 */
public class StockDBAccess {
    
    private static final String INSERT_ITEM = "INSERT INTO Consumables (name, quantity, expiry_date) VALUES (?, ?, ?)";
    private static final String GET_ITEM = "SELECT * FROM Consumables WHERE name = ?";
    private static final String DELETE_ITEM = "DELETE FROM Consumables WHERE name = ?";
    private static final String GET_ALL_ITEMS = "SELECT * FROM Consumables";

    // Adds stock item to db
    public static boolean addStockItem(StockItem item) {
        if (itemExists(item.getName())) {
            return false; // Item already exists
        }
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM)) {

            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDate(3, Date.valueOf(item.getExpiryDate()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // Checks if an item with the given name already exists in the database.
    public static boolean itemExists(String name) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ITEM)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If the result set has data, the item exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Gets single item by name
    public static StockItem getStockItemByName(String name) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ITEM)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new StockItem(
                        rs.getInt("consumable_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Item not found
    }
    
    public static boolean updateStockQuantity(String name, int newQuantity) {
        String sql = "UPDATE Consumables SET quantity = ? WHERE name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newQuantity);
            stmt.setString(2, name);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Reads all stock from db
    public static List<StockItem> getAllStockItems() {
        
        List<StockItem> stockList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_ITEMS)) {

            while (rs.next()) {
                StockItem item = new StockItem(
                        rs.getInt("consumable_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate()
                );
                stockList.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    // Alter a stock item's information
    public static boolean updateStockItem(String currentName, String newName, Integer quantity, LocalDate expiryDate) {
        
        StringBuilder query = new StringBuilder("UPDATE Consumables SET ");
        boolean hasUpdates = false;

        if (newName != null) {
            query.append("name = ?, ");
            hasUpdates = true;
        }
        if (quantity != null) {
            query.append("quantity = ?, ");
            hasUpdates = true;
        }
        if (expiryDate != null) {
            query.append("expiry_date = ?, ");
            hasUpdates = true;
        }

        if (!hasUpdates) {
            return false; // If nothing has been entered to change
        }

        query.setLength(query.length() - 2); // Remove comma
        query.append(" WHERE name = ?");

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (newName != null) {
                stmt.setString(paramIndex++, newName);
            }
            if (quantity != null) {
                stmt.setInt(paramIndex++, quantity);
            }
            if (expiryDate != null) {
                stmt.setDate(paramIndex++, Date.valueOf(expiryDate));
            }

            stmt.setString(paramIndex, currentName);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Finds products expiring within X days
    public static List<StockItem> getExpiringItems(int days) {
        List<StockItem> items = new ArrayList<>();
        String query = "SELECT * FROM consumables WHERE expiry_date <= CURDATE()"
                + " + INTERVAL ? DAY";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(new StockItem(
                    rs.getInt("consumable_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDate("expiry_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Finds low stock products (below threshold)
    public static List<StockItem> getLowStockItems(int threshold) {
        List<StockItem> items = new ArrayList<>();
        String query = "SELECT * FROM consumables WHERE quantity < ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(new StockItem(
                    rs.getInt("consumable_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDate("expiry_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    
    // Removes stock item from db
    public static boolean deleteStockItem(String name) {
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ITEM)) {

            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

