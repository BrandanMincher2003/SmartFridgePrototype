package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.database.UserDBAccess;
import com.mycompany.ffsmartapplication.models.StockItem;
import com.mycompany.ffsmartapplication.models.User;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import com.mycompany.ffsmartapplication.utils.NotificationUtils;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
        AlertUtils.showAlert(Alert.AlertType.WARNING, "Missing Input", "Please enter both username and password.");
        return;
    }

        // Authenticate user from the database
        User authenticatedUser = UserDBAccess.authenticateUser(user, pass);

        if (authenticatedUser != null) {
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + authenticatedUser.getUsername() + "!");

            // Store logged-in user in App for future use
            App.setCurrentUser(authenticatedUser);
            
            // Check for notifications if Head Chef logs in
            if ("Head Chef".equalsIgnoreCase(authenticatedUser.getRole())) {
                NotificationUtils.checkExpiryWarnings();
                NotificationUtils.checkLowStockWarnings();
            }

            try {
                App.setRoot("mainDashboard"); // Navigate to main dashboard
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void checkExpiryWarnings() {
        List<StockItem> expiringItems = StockDBAccess.getExpiringItems(3); // Fetch from DB

        if (!expiringItems.isEmpty()) {
            StringBuilder message = new StringBuilder("Items expiring soon:\n");
            for (StockItem item : expiringItems) {
                message.append(String.format("- %s (ID: %d, Quantity: %d, Expiry: %s)\n",
                        item.getName(), item.getItemId(), item.getQuantity(), item.getExpiryDate()));
            }
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Expiry Warning", message.toString());
        }
    }
    
    private void checkLowStockWarnings() {
        List<StockItem> lowStockItems = StockDBAccess.getLowStockItems(10); // Fetch from DB

        if (!lowStockItems.isEmpty()) {
            StringBuilder message = new StringBuilder("Low stock alert:\n");
            for (StockItem item : lowStockItems) {
                message.append(String.format("- %s (ID: %d, Quantity: %d)\n",
                        item.getName(), item.getItemId(), item.getQuantity()));
            }
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Low Stock Alert", message.toString());
        }
    }
    
    @FXML
    private void handleBack() throws IOException {
        App.setRoot("startpage");
    }
}
