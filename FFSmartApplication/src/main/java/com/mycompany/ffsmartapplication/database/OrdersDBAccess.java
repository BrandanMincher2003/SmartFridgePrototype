/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.database;

import com.mycompany.ffsmartapplication.models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Damola
 */
public class OrdersDBAccess {
    private static final String INSERT_ORDER = "INSERT INTO orders (item_name, quantity, supplier) VALUES (?, ?, ?)";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE order_number = ?";
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE order_number = ?";

    // Add a new order
    public void addOrder(Order order) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, order.getItemName());
            pstmt.setString(2, order.getQuantityString());
            pstmt.setString(3, order.getSupplier());
            pstmt.executeUpdate();

            // Get generated order number
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setOrderNumber(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an order by ID
    public void deleteOrder(int orderNumber) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ORDER)) {
            pstmt.setInt(1, orderNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all orders
    public List<Order> getAllOrders() {
        List<Order> ordersList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT order_number, item_name, quantity, supplier FROM orders");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_number"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getString("supplier")
                );
                ordersList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    // Get a specific order by ID
    public Order getOrderById(int orderNumber) {
        Order order = null;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ORDER_BY_ID)) {
            pstmt.setInt(1, orderNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(
                            rs.getInt("order_number"),
                            rs.getString("item_name"),
                            rs.getInt("quantity"),
                            rs.getString("supplier")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }
}
