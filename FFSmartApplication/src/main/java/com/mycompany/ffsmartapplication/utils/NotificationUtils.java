/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ffsmartapplication.utils;

import com.mycompany.ffsmartapplication.database.NotificationDBAccess;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.models.StockItem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.List;
    
/**
 *
 * @author Damola
 */
public class NotificationUtils {

    public static void checkExpiryWarnings() {
        List<StockItem> expiringItems = StockDBAccess.getExpiringItems(3);
        for (StockItem item : expiringItems) {
            String message = String.format("Item expiring soon: %s (ID: %d, Qty: %d, Expiry: %s)",
                    item.getName(), item.getItemId(), item.getQuantity(), item.getExpiryDate());

            NotificationDBAccess.logNotification(item.getItemId(), "expiry_warning", message);
        }
    }

    public static void checkLowStockWarnings() {
        List<StockItem> lowStockItems = StockDBAccess.getLowStockItems(10);
        for (StockItem item : lowStockItems) {
            String message = String.format("Low stock alert: %s (ID: %d, Qty: %d)",
                    item.getName(), item.getItemId(), item.getQuantity());

            NotificationDBAccess.logNotification(item.getItemId(), "low_stock_warning", message);
        }
    }

    public static void startRealTimeNotifications() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            checkExpiryWarnings();
            checkLowStockWarnings();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
