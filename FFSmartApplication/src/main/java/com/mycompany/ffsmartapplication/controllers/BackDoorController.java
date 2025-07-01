package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.models.StockItem;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BackDoorController {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private DatePicker expiryDatePicker;  
    @FXML
    private TextField orderNumberField; // New TextField for order number
    
    @FXML
    private void handleDeposit() {
        String itemName = itemNameField.getText().trim();
        String qtyText = quantityField.getText().trim();
        LocalDate expiryDate = expiryDatePicker.getValue();
        String orderNum = orderNumberField.getText().trim();

        if (itemName.isBlank() || qtyText.isBlank()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required.");
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
        
        // If an item with this name exists, we simply update the stock (ignoring new expiry).
        // Check if item exists
        StockItem existingItem = StockDBAccess.getStockItemByName(itemName);

        if (existingItem != null) {
            // Update quantity
            int newQuantity = existingItem.getQuantity() + qty;
            boolean success = StockDBAccess.updateStockQuantity(itemName, newQuantity);

            if (success) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Stock Updated", 
                    String.format("Deposited %d of %s. New quantity: %d.\nOrder #: %s", 
                        qty, itemName, newQuantity, orderNum));
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update stock.");
            }
        } else {
            // For brand-new items, we require an expiry date from the user
            if (expiryDate == null) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, "Missing Expiry Date", "Please select an expiry date for a new item.");
                return;
            }

            StockItem newItem = new StockItem(itemName, qty, expiryDate);

            boolean added = StockDBAccess.addStockItem(newItem);
            if (added) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "New Item Added", 
                    String.format("%s added with quantity %d (expires on %s).\nOrder #: %s", 
                        itemName, qty, expiryDate, orderNum));
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add new item.");
            }
        }

        // Clear input fields
        itemNameField.clear();
        quantityField.clear();
        expiryDatePicker.setValue(null);
        orderNumberField.clear();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainDashboard");
    }
}
