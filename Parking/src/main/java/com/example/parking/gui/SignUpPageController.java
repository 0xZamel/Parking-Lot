package com.example.parking.gui;
import com.example.parking.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignUpPageController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField licenseNumberField;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;
    @FXML
    private TextField licensePlateField;
    @FXML
    private ListView<String> vehicleListView;
    @FXML
    private Button signUpButton;
    @FXML
    private Hyperlink loginLink;
    // ObservableList to store vehicles
    private final ObservableList<String> vehicles = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        // Initialize the ListView with the vehicles list
        vehicleListView.setItems(vehicles);
    }

    @FXML
    public void handleAddVehicle() {
        // Get input values
        String vehicleType = vehicleTypeComboBox.getValue();
        String licensePlate = licensePlateField.getText().trim();

        // Validate inputs
        if (vehicleType == null || vehicleType.isEmpty()) {
            showAlert("Error", "Please select a vehicle type.");
            return;
        }
        if (licensePlate.isEmpty()) {
            showAlert("Error", "Please enter a license plate.");
            return;
        }

        if (vehicles.size() >= 3) {
            showAlert("Error", "You can only add up to 3 vehicles.");
            return;
        }

        // Add the vehicle to the list
        String vehicleInfo = "Vehicle: " + vehicleType + ", Plate: " + licensePlate;
        vehicles.add(vehicleInfo);

        // Clear the input fields
        licensePlateField.clear();
        vehicleTypeComboBox.setValue(null);
    }

    @FXML
    public void handleRemoveVehicle() {
        // Get the selected vehicle
        String selectedVehicle = vehicleListView.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            return;
        }

        // Remove the selected vehicle
        vehicles.remove(selectedVehicle);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void signUpButtonClicked(ActionEvent event) {
        // Get input values
        String userName = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String licenseNumber = licenseNumberField.getText().trim();

        if (userName.isEmpty() || password.isEmpty() || licenseNumber.isEmpty()) {
            showAlert("Error", "All fields must be filled");
            return;
        }

        // if the Username Taken Error
        if (SystemManager.isUserNameExist(userName)) {
            showAlert("Error", "This Username Is Taken");
            return;
        }

        // Register Successfully
        showAlert("Success", "signUp Successfully!");
        // SystemManager.register(userName, password, licenseNumber, vehicles, balance);

        // Go LoginPage
        goToLoginPage(event);
    }

    @FXML
     public void goToLoginPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/parking/LoginPageFXML.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the login page xfml.");
        }
    }
}