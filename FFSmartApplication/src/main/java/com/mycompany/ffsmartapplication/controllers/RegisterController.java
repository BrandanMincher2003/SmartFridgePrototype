package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.database.UserDBAccess;
import com.mycompany.ffsmartapplication.models.User;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

   
    @FXML
    public void initialize() {
        // Populate the role combo box with roles
        roleComboBox.getItems().addAll("Chef", "Delivery Person", "Head Chef", "Admin");
        // Optionally select a default
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String role = roleComboBox.getValue();

        // validation placeholder
        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            AlertUtils.showAlert(AlertType.INFORMATION, "Blank field(s)", "Please fill out all fields");
        }
       
        // Create user object with details provided
        User newUser = new User(username, password, role);
        String result = UserDBAccess.registerUser(newUser);
        
        switch (result) {
            case "success":
                AlertUtils.showAlert(AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
                break;
            case "username_exists":
                AlertUtils.showAlert(AlertType.ERROR, "Registration Failed", "Username already exists. Please choose another.");
                break;
            default:
                AlertUtils.showAlert(AlertType.ERROR, "Registration Failed", "An unexpected error occurred.");
                break;
        }
        
        

        // go back to the login screen
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("startpage");
    }
    
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        roleComboBox.setValue(null);
    }
}
