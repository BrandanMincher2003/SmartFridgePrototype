package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.OrdersDBAccess;
import com.mycompany.ffsmartapplication.models.Order;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeliveriesController {

    @FXML
    private TableView<Order> deliveriesTable;
    @FXML
    private TableColumn<Order, Integer> colOrderNumber;
    @FXML
    private TableColumn<Order, String> colItemName;
    @FXML
    private TableColumn<Order, Integer> colQuantity;
    @FXML
    private TableColumn<Order, String> colSupplier;

    private ObservableList<Order> orderData = FXCollections.observableArrayList();
    
    private OrdersDBAccess ordersDBAccess = new OrdersDBAccess();

    @FXML
    public void initialize() {

        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        loadDeliveries(); // Load data from database
    }

    private void loadDeliveries() {
        orderData.clear(); // List is cleared before adding
        List<Order> pendingOrders = ordersDBAccess.getAllOrders(); // Fetch orders

        orderData.addAll(pendingOrders); // Use addAll() instead of loop
        deliveriesTable.setItems(orderData);
    }


    @FXML
    private void handleBack() throws IOException {
        App.setRoot("admin");
    }
}
