package com.example.parking;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Feedback
{
    private int rate;
    private String feedbackMessage;
    private final int ownerID;
    private final int ReservationID;

    public Feedback(int ownerID, int ReservationID)
    {
        IDsCheck(ownerID,ReservationID);
        this.ownerID=ownerID;
        this.ReservationID=ReservationID;
    }

    public void setFeedback(int rate, String feedbackMessage)
    {
        this.rate=rate;
        this.feedbackMessage=feedbackMessage;
        saveFeedbackToFile();


    }

    public void displayFeedbacks()
    {
        System.out.println("Feedback: ");
        System.out.println("Rating: "+rate+" out of 5");
        System.out.println("Feedback comment: "+ feedbackMessage);
    }
    public int getRate() {
        return rate;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public int getOwenerID() {
        return ownerID;
    }

    public int getReservationID() {
        return ReservationID;
    }

    private void IDsCheck(int ownerID, int ReservationID)
    {
        if(ownerID<1 || ReservationID<1)
        {
            throw  new IllegalArgumentException("Invalid ID Number! Please try again");
        }
    }


    private void saveFeedbackToFile() {
        String fileName = "feedbacks.txt";
        FileOutputStream myFile = null;
        PrintStream print =null;
        try{
           myFile= new FileOutputStream(fileName,true);
           print = new PrintStream(myFile);
            print.println("Owner ID: "+ownerID + " ,Reservation ID: "+ ReservationID);
            print.println("Rating: "+ rate +" Out of 5");
            print.println("Feedback comment: "+ feedbackMessage);
            System.out.println("========================================");

        }catch (IOException e){
            System.out.println("Error saving feedback to file: " + e.getMessage());

        }
        finally {

               try {
                   if(myFile!= null)
                      myFile.close();
                   if (print != null)
                       print.close();
               }catch(Exception ex){
                   System.out.println("Error closing file, "+ex.getMessage());
            }
        }

    }
}
