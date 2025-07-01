package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.utils.AlertUtils;
import com.mycompany.ffsmartapplication.models.StockItem;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.models.User;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FrontDoorController {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox<String> actionComboBox;

    @FXML
    public void initialize() {
        // block "Deliverer" role:
        User user = App.getCurrentUser();
        if (user != null && "Deliverer".equalsIgnoreCase(user.getRole())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Deliverers cannot access Front Door.");
            alert.showAndWait();
            try {
                App.setRoot("mainDashboard");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Populate action combo box with Withdraw/Deposit
        actionComboBox.getItems().addAll("Withdraw", "Deposit");
        actionComboBox.getSelectionModel().selectFirst(); // default
    }

    @FXML
    private void handleConfirm() {
        String itemName = itemNameField.getText().trim();
        String qtyText = quantityField.getText().trim();
        String action = actionComboBox.getValue();

        if (itemName.isBlank() || qtyText.isBlank()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be a number.");
            return;
        }

        // Check if item exists
        StockItem existingItem = StockDBAccess.getStockItemByName(itemName);

        if (existingItem != null) {
            // Update stock quantity
            int newQuantity = action.equals("Withdraw") ? existingItem.getQuantity() - qty : existingItem.getQuantity() + qty;

            if (newQuantity < 0) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Stock Error", "Not enough stock to withdraw.");
                return;
            }

            boolean success = StockDBAccess.updateStockQuantity(itemName, newQuantity);

            if (success) {
                String message = action.equals("Withdraw") ?
                    String.format("Withdrew %d of %s.", qty, itemName) :
                    String.format("Deposited %d of %s.", qty, itemName);
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", message);
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update stock.");
            }
        } else {
            if (action.equals("Withdraw")) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Item Not Found", "No item to withdraw.");
            } else {
                LocalDate defaultExpiry = LocalDate.now().plusDays(30); // Default expiry
                StockItem newItem = new StockItem(itemName, qty, defaultExpiry);

                boolean added = StockDBAccess.addStockItem(newItem);
                if (added) {
                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "New Item Added", 
                        String.format("%s added with quantity %d.", itemName, qty));
                } else {
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add new item.");
                }
            }
        }

        // Clear input fields
        itemNameField.clear();
        quantityField.clear();
        actionComboBox.getSelectionModel().selectFirst();
    }


    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainDashboard");
    }
}

