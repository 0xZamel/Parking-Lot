package com.example.parking.gui;

import com.example.parking.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.stage.*;
import org.controlsfx.control.*;

import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class UserPageController {
    private Owner owner;

    private ScrollPane lastScrollPane = new ScrollPane();
    private Pane lastPane = new Pane();

    // Home
    @FXML
    private Label headerText;
    @FXML
    private Pane homePane, reservationPane, depositPane, updateDataPane,backPane, updateReservationPane;
    @FXML
    private BorderPane feedbackPane;
    @FXML
    private ScrollPane makeReservationPane;


    // Feedback
    @FXML
    private Label submit_msg;
    @FXML
    private TextField ReservationID_field;
    @FXML
    private Rating RatingBar;
    @FXML
    private TextArea feedbackMessage;
    @FXML
    private Button submit_btn;
    private boolean isFeedbackSubmitted = false;

    // Deposit
    @FXML
    private TextField depositAmountField;
    @FXML
    private Label  balanceLabel;
    @FXML
    private Button depositButton;

    // Reservations
    @FXML
    private TableView<Reservation> ReservationTable;
    @FXML
    private GridPane gridPaneSlots;


    public void initialize() {
        owner = LoginPageController.getCurrentOwner();
        homePane.setVisible(false);
        reservationPane.setVisible(false);
        depositPane.setVisible(false);
        feedbackPane.setVisible(false);
        updateDataPane.setVisible(false);
        backPane.setVisible(false);
        makeReservationPane.setVisible(false);
        updateReservationPane.setVisible(false);

        headerText.setText("Welcome " + owner.getUserName());
        switchToPane(homePane);
    }

    private void switchToPane(Pane pane) {
        backPane.setVisible(false);

        lastPane.setVisible(false);
        lastScrollPane.setVisible(false);
        pane.setVisible(true);
        lastPane = pane;
    }
    private  void switchToPane(ScrollPane pane) {
        backPane.setVisible(false);

        lastPane.setVisible(false);
        lastScrollPane.setVisible(false);
        pane.setVisible(true);
        lastScrollPane = pane;
    }

    private void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.getDialogPane().getStyleClass().add("user-alert");

        URL cssFile = getClass().getResource("/css/adminStyle.css");
        if (cssFile != null) {
            alert.getDialogPane().getStylesheets().add(cssFile.toExternalForm());
        } else {
            System.out.println("CSS file not found.");
        }

        alert.showAndWait();
    }

    public void handleBackButton() {
        switchToPane(reservationPane);
    }

    // Left Buttons
    // Home Page
    @FXML
    public void goToHomePage() {
        headerText.setText("Welcome " + owner.getUserName());
        switchToPane(homePane);
    }

    // Reservations Page
    public void chooseReservations() {
        headerText.setText("Reservation Management");
        switchToPane(reservationPane);
    }

    // Make Reservation
    public void goToMakeReservation(){
        switchToPane(makeReservationPane);
        headerText.setText("Make a New Reservation");
        backPane.setVisible(true);
        makeReservationView();
    }
    private ArrayList<Slot> selectedSlots;
    public void makeReservationView() {
        gridPaneSlots.getChildren().clear(); // Clear all previous content
        selectedSlots = new ArrayList<>();

        ArrayList<Slot> slots = SystemManager.getAvailableSlots(owner);
        int columns = 3; // Slots per row

        // Create a label to display the Total Amount
        Label totalAmountLabel = new Label("Total Amount: $" + owner.getPayment().getPenalty());
        totalAmountLabel.getStyleClass().add("total-amount-label");
        gridPaneSlots.add(totalAmountLabel, 0, 0, columns, 1); // Add the total amount label above the slots

        for (int i = 0; i < slots.size(); ++i) {
            Slot slot = slots.get(i);
            VBox slotBox = createSlotBox(slot, totalAmountLabel);

            slotBox.setAlignment(Pos.CENTER); // Centers content within the VBox
            slotBox.setSpacing(10);
            slotBox.getStyleClass().add("slot-box");

            int row = i / columns;
            int col = i % columns;
            gridPaneSlots.add(slotBox, col, row + 1); // Offset by 1 to leave space for the total amount label
        }

        // Create a button to confirm reservation
        Button makeReservationButton = new Button("Make Reservation");
        makeReservationButton.setOnAction(event -> showConfirmationDialog(totalAmountLabel.getText()));

        gridPaneSlots.setHgap(18); // Horizontal gap between columns
        gridPaneSlots.setVgap(15); // Vertical gap between rows

        // Add the button below the ScrollPane
        HBox buttonContainer = new HBox(10, makeReservationButton);
        buttonContainer.setAlignment(Pos.CENTER);
        gridPaneSlots.add(buttonContainer, 0, slots.size() / 4 + 2, 4, 1); // Add below grid
    }
    private VBox createSlotBox(Slot slot, Label totalAmountLabel) {
        String   spotType = slot.getVehicleType() + " Spot";
        int spotID = slot.getSpotID();
        LocalDateTime startDate = slot.getStartDate();
        LocalDateTime endDate = slot.getEndDate();
        double hours = slot.getHours();
        double amount = slot.getAmount();

        // Header (Spot ID)
        Label labelSpotId = new Label(spotType);
        labelSpotId.setMaxWidth(Double.MAX_VALUE);
        labelSpotId.setAlignment(Pos.CENTER);
        labelSpotId.getStyleClass().add("slot-header");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Adjust format as needed
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        // Body Rows
        HBox slotRow = createDetailRow("Spot:", String.valueOf(spotID));
        HBox startDateRow = createDetailRow("Start:", formattedStartDate);
        HBox endDateRow = createDetailRow("End:", formattedEndDate);
        HBox hoursRow = createDetailRow("Hours:", String.format("%.2f", hours));

        VBox body = new VBox(5, slotRow, startDateRow, endDateRow, hoursRow);

        // Footer (Amount on the left, Checkbox on the right)
        Label amountLabel = new Label("Amount: $" + String.format("%.2f", amount));
        amountLabel.getStyleClass().add("amount-label");

        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("slot-checkbox");
        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                selectedSlots.add(slot);
            }
            else {
                selectedSlots.remove(slot);
            }
            double currentTotalAmount = Payment.totalAmount(owner.getPayment(), selectedSlots) + owner.getPayment().getPenalty();
            totalAmountLabel.setText("Total Amount: $" + String.format("%.2f", currentTotalAmount));
        });

        // Spacer to separate amount and checkbox
        Region spacer = new Region();
        spacer.setMinWidth(1); // Adjust spacing as needed
        HBox.setHgrow(spacer, Priority.ALWAYS); // Expands to push elements apart

        HBox footer = new HBox(1, amountLabel, spacer, checkBox);
        footer.getStyleClass().add("slot-footer");

        // Slot Box Layout
        VBox slotBox = new VBox(labelSpotId, body, footer);
        slotBox.getStyleClass().add("slot-box");

        return slotBox;
    }

    private HBox createDetailRow(String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("slot-label");

        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("slot-value");

        HBox row = new HBox(10, labelNode, valueNode);
        row.getStyleClass().add("slot-row");

        return row;
    }
    private void showConfirmationDialog(String totalAmountText) {
        double penalty = owner.getPayment().getPenalty();
        double amount = Payment.totalAmount(owner.getPayment(), selectedSlots);
        double balance = owner.getPayment().getBalance();

        double totalAmount = amount + penalty;
        // Proceed with reservation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reservation Confirmation");
        alert.setHeaderText("Reservation Details");
        alert.setContentText(String.format("Amount: $%.2f\nPenalty: $%.2f\nFinal Amount: $%.2f\n",
                amount, penalty, totalAmount));

        // Apply the dialog's custom CSS
        alert.getDialogPane().getStyleClass().add("dialog-content");
        ButtonType okButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                if (balance < totalAmount) {
                    // Handle insufficient balance
                    Alert newAlert = new Alert(Alert.AlertType.ERROR, "Insufficient balance. Please add more funds");
                    newAlert.getDialogPane().getStyleClass().add("dialog-content"); // Apply dialog content CSS
                    newAlert.showAndWait();
                    return;
                }
                for (Slot slot : selectedSlots) {
                    owner.makeReservation(slot);
                }
                Alert newAlert = new Alert(Alert.AlertType.INFORMATION, "Reservations Made Successfully");
                newAlert.getDialogPane().getStyleClass().add("dialog-content"); // Apply dialog content CSS
                newAlert.showAndWait();
                makeReservationView();
            }
        });
    }

    // Update Reservation
    public void goToUpdateReservationPane() {
        switchToPane(updateReservationPane);
        headerText.setText("Reservations History");
        backPane.setVisible(true);
        updateReservationView();
    }
    public void updateReservationView() {
        // to prevent duplication
        ReservationTable.getColumns().clear();
        ReservationTable.getItems().clear();
        ReservationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<Reservation> reservations = owner.getReservations();

        TableColumn<Reservation, String> ownerIdCol = new TableColumn<>("Owner ID");
        ownerIdCol.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getOwnerID())));

        TableColumn<Reservation, String> reservationIdCol = new TableColumn<>("Reservation ID");
        reservationIdCol.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getReservationID())));

        TableColumn<Reservation, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(String.format("%.2f", param.getValue().getAmount()))));

        // To Show The Start Date , End Datefad
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Start Date Column
        TableColumn<Reservation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getReservationDate().format(formatter))
        );

        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(param ->
                new SimpleStringProperty((param.getValue().getStatus() ? "Confirmed" : "Canceled"))
        );

        ReservationTable.getColumns().addAll(ownerIdCol, reservationIdCol, amountCol, dateCol, statusCol);
        ReservationTable.getItems().addAll(reservations);
    }

    // Deposit Page
    public void goToDepositPage() {
        switchToPane(depositPane);
        headerText.setText("Deposit Management");
        depositView();
    }
    public void depositView() {
        try {
            double balance = owner.getPayment().getBalance();
            balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));

            depositAmountField.clear();
            depositButton.setOnAction(event -> {
                String depositAmountText = depositAmountField.getText().trim();
                if (depositAmountText.isEmpty()) {
                    showAlert("Message","Error", "Deposit amount cannot be empty.");
                    return;
                }
                try {
                    double depositAmount = Double.parseDouble(depositAmountText);
                    if (depositAmount <= 0) {
                        showAlert("Message","Error", "Deposit amount must be greater than zero.");
                        return;
                    }
                    owner.getPayment().deposit(depositAmount);
                    balanceLabel.setText("Current Balance: $" + String.format("%.2f", owner.getPayment().getBalance()));
                    showAlert("Message","Success", "Funds added successfully.");
                    depositAmountField.clear();
                } catch (NumberFormatException e) {
                    showAlert("Message","Error", "Invalid amount entered. Please enter a valid number.");
                }
            });
        } catch (Exception e) {
            showAlert("Message","Error", "An error occurred while loading the deposit view.");
        }
    }

    // Feedback Page
    public void goToFeedbackPage() {
        headerText.setText("Make Feedback");
        switchToPane(feedbackPane);
    }
    public void feedbackSubmitButton() {
        if (isFeedbackSubmitted) {
            return;
        }
        try {

            String reservationIdText = ReservationID_field.getText();
            String message = feedbackMessage.getText();
            double rate = RatingBar.getRating();

            if (reservationIdText.isEmpty()) {
                submit_msg.setText("Reservation ID cannot be empty!");
                return;
            }
            if (message.isEmpty()) {
                submit_msg.setText("Feedback message cannot be empty!");
                return;
            }

            int reservationID = Integer.parseInt(reservationIdText);
            boolean isReservationExist = false;
            for (Reservation reservation : owner.getReservations()) {
                if (reservation.getReservationID() == reservationID) {
                    isReservationExist = true;
                }
            }

            if (!isReservationExist) {
                submit_msg.setText("There Is No reservation with that ID");
                return;
            }

            owner.makeFeedback(owner.getOwnerID(), reservationID, rate, message);
            isFeedbackSubmitted = true;

            disableInputs();
            submit_msg.setText("Your feedback has been submitted successfully!");
        }  catch (Exception e) {
            submit_msg.setText("Reservation ID Must be a number");
        }
    }
    private void disableInputs() {
        ReservationID_field.setDisable(true);
        RatingBar.setDisable(true);
        feedbackMessage.setDisable(true);
        submit_btn.setDisable(true);
    }

    // Update Owner Data Page
    public void goToUpdateDataPage(){
        switchToPane(updateDataPane);
        headerText.setText("Update Data");
        UpdateDataView();
    }
    public void UpdateDataView() {
        // Prevent duplication
        updateDataPane.getChildren().clear();

        // Username
        Label userNameLabel = new Label("Username:");
        userNameLabel.getStyleClass().add("form-label");

        TextField userNameField = new TextField(owner.getUserName());
        userNameField.setPromptText("Enter new username");
        userNameField.getStyleClass().add("form-textfield");

        // My Password
        Label myPasswordLabel = new Label("Password:");
        myPasswordLabel.getStyleClass().add("form-label");

        PasswordField myPasswordField = new PasswordField();
        myPasswordField.setPromptText("Enter your Password");
        myPasswordField.getStyleClass().add("form-textfield");

        // Red Star Label
        Label redStar = new Label("*");
        redStar.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");

        // HBox to combine PasswordField and Red Star
        HBox myPasswordFieldBox = new HBox();
        myPasswordFieldBox.getChildren().addAll(myPasswordField, redStar);
        myPasswordFieldBox.setAlignment(Pos.CENTER_LEFT);
        myPasswordFieldBox.setSpacing(3); // Adjust spacing between the field and star

        // Confirm Password
        Label newPasswordLabel = new Label("New Password :");
        newPasswordLabel.getStyleClass().add("form-label");

        // New Password
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        newPasswordField.getStyleClass().add("form-textfield");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.getStyleClass().add("form-textfield");

        // License Number
        Label licenseLabel = new Label("License Number:");
        licenseLabel.getStyleClass().add("form-label");

        TextField licenseField = new TextField(owner.getLicenseNumber());
        licenseField.setPromptText("Enter license number");
        licenseField.getStyleClass().add("form-textfield");

        // Update Profile Button
        Button updateButton = new Button("Update Profile");
        updateButton.setOnAction(event -> handleUpdateProfile(
                userNameField, myPasswordField, newPasswordField, confirmPasswordField, licenseField));
        updateButton.getStyleClass().add("update-button");

        // Form Layout
        VBox updateForm = new VBox(15,
                userNameLabel, userNameField,
                myPasswordLabel, myPasswordFieldBox, newPasswordLabel,
                newPasswordField, confirmPasswordField,
                licenseLabel, licenseField,
                updateButton);
        updateForm.setAlignment(Pos.CENTER);
        updateForm.setPadding(new Insets(20));
        updateForm.getStyleClass().add("form-container");

        // Adjust positioning
        updateForm.setTranslateX(updateDataPane.getWidth() / 2 - updateForm.getWidth() / 2 - 125);
        updateForm.setTranslateY(20);

        // Center Pane
        StackPane centerPane = new StackPane(updateForm);
        centerPane.setAlignment(Pos.TOP_CENTER);
        updateDataPane.getChildren().add(centerPane);
    }
    private void handleUpdateProfile(TextField userNameField, PasswordField myPasswordField,
                                     PasswordField passwordField, PasswordField confirmPasswordField, TextField licenseField) {
        String newUserName = userNameField.getText().trim();
        String myPassword = myPasswordField.getText().trim();
        String newPassword = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String newLicenseNumber = licenseField.getText().trim();

        // Username Check
        // Empty
        if (newUserName.isEmpty()) {
            showAlert("Message", "Error", "Username Can't Be Empty!");
            return;
        }
        // Check If It's Taken
        if (!newUserName.equals(owner.getUserName()) && SystemManager.isOwnerExist(newUserName)) {
            showAlert("Message", "Error", "Username Already Exists.!");
            return;
        }
        // He Has To Enter His Old Password
        if (myPassword.isEmpty()) {
            showAlert("Message", "Error", "Please Enter Your Password To Update!");
            return;
        }
        if (!myPassword.equals(owner.getPassword())) {
            showAlert("Message", "Error", "Wrong Password!");
            return;
        }
        // Check If He Want To Update His Password
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                showAlert("Message", "Error", "Passwords do not match.");
                return;
            }
            if (newPassword.length() < 6) {
                showAlert("Message", "Error", "Password must be at least 6 characters long!");
                return;
            }
        }
        // Check That License Number Is Not Empty
        if (newLicenseNumber.isEmpty()) {
            showAlert("Message", "Error", "License Number Can't Be Empty!");
            return;
        }
        owner.setUserName(newUserName);
        if (!newPassword.isEmpty())
            owner.setPassword(newPassword);
        owner.setLicenseNumber(newLicenseNumber);
        showAlert("Message", "Success", "Profile updated successfully.");
        UpdateDataView();
    }

    // Logout
    public void logoutButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/parking/LoginPageFXML.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Message","Error", "Failed to load the login page xfml.");
        }
    }
}