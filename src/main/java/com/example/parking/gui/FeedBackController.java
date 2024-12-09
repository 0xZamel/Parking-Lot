package com.example.parking.gui;

import com.example.parking.Feedback;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.Rating;


public class FeedBackController {

    @FXML
    private Label submit_msg;

    @FXML
    private TextField OwnerID_field;

    @FXML
    private TextField ReservationID_field;

    @FXML
    private Rating RatingBar;
    @FXML
    private TextArea txt_field;

    @FXML
    private Button submit_btn;

    private Feedback feedback;

    private boolean isFeedbackSubmitted = false;

    @FXML
    private void handleSubmit() {

        if (isFeedbackSubmitted) {
            return;
        }

        try {

            int ownerID = Integer.parseInt(OwnerID_field.getText());
            int reservationID = Integer.parseInt(ReservationID_field.getText());

            feedback = new Feedback(ownerID, reservationID);

            int rate = (int) RatingBar.getRating();
            String message = txt_field.getText();


            if (message.isEmpty()) {
                submit_msg.setText("Feedback message cannot be empty!");
                return;
            }


            feedback.setFeedback(rate, message);
            isFeedbackSubmitted = true;

            disableInputs();
            submit_msg.setText("Your feedback has been submitted successfully!");

        } catch (NumberFormatException e) {
            submit_msg.setText("Invalid ID Number! Please try again");
        }
        catch (IllegalArgumentException e) {
            submit_msg.setText(e.getMessage());
        }
    }


    private void disableInputs() {
        OwnerID_field.setDisable(true);
        ReservationID_field.setDisable(true);
        RatingBar.setDisable(true);
        txt_field.setDisable(true);
        submit_btn.setDisable(true);
    }
}