package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.UserDBAccess;
import com.mycompany.ffsmartapplication.models.User;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class UserDetailsController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> colUserId;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, Integer> colAuthority;

    private ObservableList<User> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAuthority.setCellValueFactory(new PropertyValueFactory<>("authorityLevel"));

        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        List<User> users = UserDBAccess.getAllUsers();
        userData.setAll(users);
        userTable.setItems(userData);
    }

    @FXML
    private void handleCreateUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Create New User");
        dialog.setHeaderText("Enter user details");
        ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("Admin", "Head Chef", "Chef", "Delivery Person"));

        GridPane grid = new GridPane();
        grid.add(new Label("Username"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role"), 0, 2);
        grid.add(roleCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == createButton) {
                return new User(usernameField.getText(), passwordField.getText(), roleCombo.getValue());
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            if (UserDBAccess.registerUser(user).equals("success")) {
                userData.add(user);
            } else {
                new Alert(Alert.AlertType.ERROR, "Error adding user.").showAndWait();
            }
        });
        loadUsersFromDatabase();
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if (UserDBAccess.deleteUser(selectedUser.getId())) {
                userData.remove(selectedUser);
            } else {
                new Alert(Alert.AlertType.ERROR, "Error deleting user.").showAndWait();
            }
        }
        loadUsersFromDatabase();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("admin");
    }
}
