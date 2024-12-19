package com.example.parking.gui;

import com.example.parking.Owner;
import com.example.parking.Vehicle;
import com.example.parking.VehicleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class menuController {
    private static Owner currentOwner = new Owner("Fady", "123456", 1, "AA" ,
            new ArrayList<>(), 5000);

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label login_msg;
    @FXML
    private Pane buttonHolder;
    @FXML
    private AnchorPane loginPage, signUpPage;

    @FXML
    private TextField usernameField2, licenseNumberField, licensePlateField, balanceField;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;
    @FXML
    private ListView<String> vehicleListView;
    @FXML
    private Label signup_msg;

    // ObservableList To Store Vehicles To Print Them
    private ObservableList<String> vehiclesString = FXCollections.observableArrayList();
    // To Store Vehicles Into The Owner
    private ArrayList<Vehicle> vehicles = new ArrayList<>();


    private Pane currentPane;
    private AnchorPane currentAnchorPane;
    @FXML
    void initialize() {
        currentPane = new Pane();
        currentAnchorPane = new AnchorPane();

        buttonHolder.setVisible(true);
        loginPage.setVisible(false);
        signUpPage.setVisible(false);
    }

    void switchToPane(Pane pane) {
        currentPane.setVisible(false);
        currentAnchorPane.setVisible(false);

        pane.setVisible(true);
        currentPane = pane;
    }
    void switchToPane(AnchorPane pane) {
        currentPane.setVisible(false);
        currentAnchorPane.setVisible(false);

        pane.setVisible(true);
        currentAnchorPane = pane;
    }

    public void goToLoginPage() {
        switchToPane(loginPage);
    }
    public void goToSignUpPage() {
        switchToPane(signUpPage);
    }
    public void goToMenu() {
        switchToPane(buttonHolder);
    }

    public void loginButton(ActionEvent event) throws IOException {
        // check the User_name And Password ->
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation: Check if username or password are empty
        if (username.isEmpty() || password.isEmpty()) {
            login_msg.setText("fill in both username and password");
            return;
        }

        // Go to Admin Page
        if (username.equals("admin") && password.equals("admin")) {
            showAlert("Message", "Logged Successfully","Welcome Admin!");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/parking/AdminPageFXML.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ( ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
            return;
        }

        // User Page
        if (Owner.isOwnerExist(username, password)) {
            // Set The Current Owner
            Owner owner = Owner.getOwner(username);

            showAlert("Message", "Logged Successfully","Welcome " + owner.getUserName() + "!");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/parking/UserPageFXML.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ( ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
            return;
        }

        // Not Found
        login_msg.setText("Wrong Username/Password");
    }

    @FXML
    public void handleAddVehicle() {
        // Get input values
        String vehicleType = vehicleTypeComboBox.getValue();
        String licensePlate = licensePlateField.getText().trim();

        // Validate inputs
        if (vehicleType == null || vehicleType.isEmpty()) {
            signup_msg.setText("Please select a vehicle type");
            return;
        }
        if (licensePlate.isEmpty()) {
            signup_msg.setText("Please enter a license plate.");
            return;
        }

        if (vehiclesString.size() >= 3) {
            signup_msg.setText("You can only add up to 3 vehicles.");
            return;
        }

        if (!licensePlateValid(licensePlate)) {
            signup_msg.setText("Please enter a valid license plate.");
            return;
        }

        // Add the vehicle to the list
        String vehicleInfo = "Vehicle: " + vehicleType + ", Plate: " + licensePlate;

        Vehicle vehicle = new Vehicle(VehicleType.valueOf(vehicleType), licensePlate);

        vehicles.add(vehicle);
        vehiclesString.add(vehicleInfo);

        // Clear the input fields
        licensePlateField.clear();
        vehicleTypeComboBox.setValue(null);
    }
    @FXML
    public void handleRemoveVehicle() {
        // Get the selected vehicle
        int selectedIndex = vehicleListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        // Remove the selected vehicle
        vehiclesString.remove(selectedIndex);
        vehicles.remove(selectedIndex);
    }

    // Validations
    private boolean licensePlateValid(String licensePlate) {
        return Vehicle.isValidLicensePlate(licensePlate);
    }
    private boolean userNameValid(String userName) {
        return !(Owner.isOwnerExist(userName));
    }
    private boolean passwordValid(String password) {
        return (password.length() >= 6);
    }
    private boolean balanceValid(String balanceText) {
        try {
            double balanceDouble = Double.parseDouble(balanceText);
            // Check Greater Than 0
            return balanceDouble >= 0;
        } catch (NumberFormatException e) {
            // If Not Number
            return false;
        }
    }
    // Sign Up Button
    public void signUpButton(ActionEvent event) {
        // Get input values
        String userName = usernameField2.getText().trim();
        String password = passwordField2.getText().trim();
        String licenseNumber = licenseNumberField.getText().trim();
        String balanceText = balanceField.getText().trim();

        if (userName.isEmpty() || password.isEmpty() || licenseNumber.isEmpty() || balanceText.isEmpty()) {
            signup_msg.setText("All fields must be filled");
            return;
        }

        // if the Username Taken Error
        if (!userNameValid(userName)) {
            signup_msg.setText("This Username Is Taken");
            return;
        }

        if(!passwordValid(password)) {
            signup_msg.setText("Password must be at least 6 characters long!");
            return;
        }

        if (!balanceValid(balanceText)) {
            signup_msg.setText("Enter A Valid Balance");
            return;
        }

        double balanceDouble = Double.parseDouble(balanceText);

        // Register Successfully
        showAlert("Message","SignUp Successfully!", "Welcome to our garage <3");
        Owner.addOwner(userName, password, licenseNumber, vehicles, balanceDouble);

        // Go LoginPage
        goToLoginPage();
    }

    private void showAlert(String title,String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.getDialogPane().getStyleClass().add("login-alert");

        URL cssFile = getClass().getResource("/css/style.css");
        if (cssFile != null) {
            alert.getDialogPane().getStylesheets().add(cssFile.toExternalForm());
        } else {
            System.out.println("CSS file not found.");
        }

        alert.showAndWait();
    }
}
