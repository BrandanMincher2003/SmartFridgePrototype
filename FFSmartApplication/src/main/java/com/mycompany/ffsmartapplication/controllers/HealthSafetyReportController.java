package com.mycompany.ffsmartapplication.controllers;

import com.mycompany.ffsmartapplication.App;
import com.mycompany.ffsmartapplication.utils.AlertUtils;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class HealthSafetyReportController {

    @FXML
    private TextArea reportTextArea;

    @FXML
    private void handleSubmitReport() {
        String reportText = reportTextArea.getText().trim();
        if (reportText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a report before submitting.").showAndWait();
            return;
        }

        // Generate timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);

        // Set default file name
        String fileName = "Health & Safety Report_" + timestamp + ".txt";

        // Set the folder path where the file will be saved
        String currentPath = new File("").getAbsolutePath();
        String folderPath = currentPath + File.separator + "reports"; 

        // Create the file object with the specified folder and file name
        File file = new File(folderPath + "\\" + fileName);

        // Ensure the folder exists
        new File(folderPath).mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(reportText);
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", "File saved as: " + file.getName());
        } catch (IOException e) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the file.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("admin");
    }
}
