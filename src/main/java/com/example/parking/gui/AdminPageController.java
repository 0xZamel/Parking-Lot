package com.example.parking.gui;

import com.example.parking.*;
import com.example.parking.spot.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminPageController {
    @FXML
    private TextField spotIDField, slotSpotIDField, slotIDField, ownerIDField;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;
    @FXML
    private Label totalAmountLabel;

    private Admin admin;

    public AdminPageController() {
        admin = new Admin("admin", "admin");
    }

    @FXML
    public void removeSpot() {
        try {
            int spotID = Integer.parseInt(spotIDField.getText());
            admin.removeSpot(spotID);
            showAlert("Success", "Spot removed successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Spot ID.");
        }
    }

    @FXML
    public void removeSlot() {
        try {
            int spotID = Integer.parseInt(slotSpotIDField.getText());
            int slotID = Integer.parseInt(slotIDField.getText());
            admin.removeSlot(slotID);
            showAlert("Success", "Slot removed successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Spot ID or Slot ID.");
        }
    }

    @FXML
    public void removeOwner() {
        try {
            int ownerID = Integer.parseInt(ownerIDField.getText());
            admin.removeOwner(ownerID);
            showAlert("Success", "Owner removed successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Owner ID.");
        }
    }

    @FXML
    public void calculateTotalAmount() {
        String vehicleTypeStr = vehicleTypeComboBox.getValue();
        if (vehicleTypeStr == null) {
            showAlert("Error", "Please select a vehicle type.");
            return;
        }

        VehicleType vehicleType;
        switch (vehicleTypeStr) {
            case "Car":
                vehicleType = VehicleType.Car;
                break;
            case "Bike":
                vehicleType = VehicleType.Bike;
                break;
            case "4x4":
                vehicleType = VehicleType.Truck;
                break;
            default:
                showAlert("Error", "Invalid vehicle type.");
                return;
        }

        double totalAmount = admin.calculateTotalAmount(vehicleType);
        totalAmountLabel.setText("Total Amount: " + totalAmount);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
