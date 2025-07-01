package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class StartpageController {

    @FXML
    private void handleLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void handleRegister() throws IOException {
        App.setRoot("register");
    }
}
