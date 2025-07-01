package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.StockDBAccess;
import com.mycompany.ffsmartapplication.models.StockItem;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StockLevelsController {

    @FXML
    private TableView<StockItem> stockTable; // StockItem is a model class
    @FXML
    private TableColumn<StockItem, Integer> colItemId;
    @FXML
    private TableColumn<StockItem, String> colItemName;
    @FXML
    private TableColumn<StockItem, Integer> colQuantity;
    @FXML
    private TableColumn<StockItem, LocalDate> colExpiryDate;

    // using an ObservableList, UI updates in real time.
    private ObservableList<StockItem> stockData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up column bindings
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        loadStockItems();
    }

    // Loads the stock items into the table view
    private void loadStockItems() {
        stockData.clear(); // Remove old data
        
        List<StockItem> stockItems = StockDBAccess.getAllStockItems();
        
        stockData.addAll(stockItems);
        stockTable.setItems(stockData);
    }
    
    @FXML
    private void handleAddProduct() {
        // Create a custom dialog
        Dialog<StockItem> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter the details for the new item");

        // Define the button types (OK / Cancel)
        ButtonType confirmButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create fields for item name, ID, quantity, and expiry date
        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("Item ID (integer)");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity (integer)");

        //Allows users to select fixed dates
        DatePicker expiryDatePicker = new DatePicker();
        expiryDatePicker.setPromptText("Expiry Date");

        // Layout: place them in a GridPane or VBox
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(itemNameField, 1, 0);

//        grid.add(new Label("Item ID:"), 0, 1);
//        grid.add(itemIdField, 1, 1);

        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);

        grid.add(new Label("Expiry Date:"), 0, 2);
        grid.add(expiryDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result when the user clicks OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                // Validate or parse fields, 
                //.trim removes trailing and leading white space
                String name = itemNameField.getText().trim();
                String idText = itemIdField.getText().trim();
                String qty = quantityField.getText().trim();
                LocalDate expiryDate = expiryDatePicker.getValue();

                if (name.isEmpty() || qty.isEmpty() || expiryDate == null) {
                    // If anything is missing, return null 
                    // show an alert or handle it differently
                    return null;
                }

                try {
//                    int itemId = Integer.parseInt(idText);
                    int quantity = Integer.parseInt(qty);

                    // Create a new StockItem
                    StockItem newItem = new StockItem(name, quantity, expiryDate);
                    System.out.println("New Stock Item: " + newItem.getName() + " | Expiry: " + newItem.getExpiryDate()); // Debugging line
                    return newItem;  // This gets returned as the dialog result 
                } catch (NumberFormatException e) {
                    // If invalid number, also return null or handle error
                    return null;
                }
            }
            return null;  // User cancelled or clicked something else
        });

        // Show the dialog and capture the result
        Optional<StockItem> result = dialog.showAndWait();

        // If OK was clicked and a valid StockItem was returned
        result.ifPresent(item -> {
        if (StockDBAccess.itemExists(item.getName())) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Duplicate Item", "An item with this name already exists.");
        } else {
            boolean success = StockDBAccess.addStockItem(item);
            if (success) {
                System.out.println("Item successfully added to database!"); // Debugging line
                stockData.add(item);
                loadStockItems();
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully.");
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add item to the database.");
            }
        }
    });
    }

    @FXML
    private void handleDeleteItem() {
        // Get selected item
    StockItem selected = stockTable.getSelectionModel().getSelectedItem();
    
    if (selected != null) {
        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this item?");
        confirmAlert.setContentText("Item: " + selected.getName());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Cal   delete function from StockDBAccess
            boolean success = StockDBAccess.deleteStockItem(selected.getName());

            if (success) {
                stockData.remove(selected);
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", "Deleted item: " + selected.getName());
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete item from database.");
            }
        }
    } else {
        AlertUtils.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to delete.");
    }
}

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("admin"); // back to the Admin main page
    }
}
