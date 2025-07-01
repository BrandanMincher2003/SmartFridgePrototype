/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.database;

import com.mycompany.ffsmartapplication.models.Notification;
import com.mycompany.ffsmartapplication.models.StockItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Damola
 */
public class NotificationDBAccess {
    
    // Prevents duplicate notifications within an hour
    private static final String CHECK_EXISTING_NOTIFICATION = "SELECT COUNT(*) "
            + "FROM notifications WHERE "
            + "consumable_id = ? AND reason = ? AND timestamp >= NOW() - "
            + "INTERVAL 1 HOUR";

    // Inserts a new notification
    private static final String INSERT_NOTIFICATION = "INSERT INTO notifications"
            + " (consumable_id, reason, message) VALUES (?, ?, ?)";
    
    // Select message from last 5 notifs
    private static final String GET_RECENT_NOTIFICATIONS = "SELECT message "
            + "FROM notifications ORDER BY timestamp DESC LIMIT 5";

    public static void logNotification(int itemId, String reason, String message) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(CHECK_EXISTING_NOTIFICATION);
             PreparedStatement insertStmt = conn.prepareStatement(INSERT_NOTIFICATION)) {

            checkStmt.setInt(1, itemId);
            checkStmt.setString(2, reason);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {  // No notification in past hour
                insertStmt.setInt(1, itemId);
                insertStmt.setString(2, reason);
                insertStmt.setString(3, message);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Fetches the last 5 notifications for display
    public static List<String> getRecentNotifications() {
        List<String> notifications = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_RECENT_NOTIFICATIONS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notifications.add(rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
