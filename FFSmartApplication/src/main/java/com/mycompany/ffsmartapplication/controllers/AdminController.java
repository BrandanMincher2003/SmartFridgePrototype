package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.models.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AdminController {

    @FXML
    public void initialise() {
        // Enforce admin-only
        User currentUser = App.getCurrentUser();
        if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getRole())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Access Denied: Admin Only.");
            alert.showAndWait();
            try {
                App.setRoot("mainDashboard");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainDashboard");
    }

    @FXML
    private void handleViewStock() throws IOException {
        App.setRoot("stockLevels");
    }

    @FXML
    private void handleUserDetails() throws IOException {
        App.setRoot("userDetails");    
    }

    @FXML
    private void handleReport() throws IOException {
        App.setRoot("healthSafetyReport");
    }

    @FXML
    private void handleDeliveries() throws IOException {
        App.setRoot("deliveries");
    }
}

