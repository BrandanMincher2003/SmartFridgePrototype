package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.NotificationDBAccess;
import com.mycompany.ffsmartapplication.database.OrdersDBAccess;
import com.mycompany.ffsmartapplication.models.Order;
import com.mycompany.ffsmartapplication.models.User;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

public class MainDashboardController {
    
    @FXML
    private GridPane mainGridPane;
    @FXML
    private Button backDoorButton;
    @FXML
    private Button frontDoorButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TableView<String> notificationsTable;
    @FXML
    private TableColumn<String, String> colMessage;
    @FXML
    private Button orderButton;

    private ObservableList<String> notificationsList = FXCollections.observableArrayList();
    
    // show/hide certain buttons
    @FXML
    public void initialize() {
        notificationsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colMessage.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        
        // User object has getRole() returning a String or an enum
        User currentUser = App.getCurrentUser();
        if (currentUser != null) {
            String role = currentUser.getRole(); 
            // If user isn't Admin disable the Admin button.
            if (!"Admin".equalsIgnoreCase(role)) {
                adminButton.setDisable(true);
                //adminButton.setVisible(false);
            }
            if ("Delivery Person".equalsIgnoreCase(role)) {
                frontDoorButton.setDisable(true);
            }
            if (!"Head Chef".equalsIgnoreCase(role)) {
                notificationsTable.setDisable(true);
                orderButton.setVisible(false);
            } else {
                colMessage.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
                orderButton.setVisible(false);
                loadNotifications();
            }
        }
    }
    
    private void loadNotifications() {
        List<String> notifications = NotificationDBAccess.getRecentNotifications();
        notificationsList.setAll(notifications);
        notificationsTable.setItems(notificationsList);
    }

    @FXML
    private void handleOrder() {
        String selectedNotification = notificationsTable.getSelectionModel().getSelectedItem();
        if (selectedNotification == null) {
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "No Notification Selected", "Please select a notification.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Order Quantity");
        dialog.setHeaderText("Enter quantity to order for: " + selectedNotification);
        dialog.setContentText("Quantity:");

        dialog.showAndWait().ifPresent(quantity -> {
            try {
                int qty = Integer.parseInt(quantity);
                OrdersDBAccess ordersDBAccess = new OrdersDBAccess();
                ordersDBAccess.addOrder(new Order(selectedNotification, qty, "Food Supplier"));
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully.");
            } catch (NumberFormatException e) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a number.");
            }
        });
    }

    @FXML
    private void handleBackDoor() throws IOException {
        App.setRoot("backDoor");
    }

    @FXML
    private void handleFrontDoor() throws IOException {
        App.setRoot("frontDoor");
    }

    @FXML
    private void handleAdmin() throws IOException {
        App.setRoot("admin");
    }

    @FXML
    private void handleLogout() throws IOException {
        // Clear current user, go back to startpage
        App.setCurrentUser(null);
        App.setRoot("startpage");
    }

}
