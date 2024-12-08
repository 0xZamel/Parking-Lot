package com.example.parking;
import java.util.*;

public class Owner extends Person {
    private final int ownerID;
    private String licenseNumber;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Reservation> reservations;
    private Payment payment;

    Owner(String userName, String password, int ownerID,  String licenseNumber, ArrayList<Vehicle> vehicles,
          double balance)
    {
        super(userName, password);

        this.ownerID = ownerID;
        this.licenseNumber = licenseNumber;
        this.vehicles = vehicles;
        this.payment = new Payment(ownerID, balance);
        this.reservations = new ArrayList<>();
    }

    void makeReservation(Reservation reservation) {
        // Confirm Payment
        payment.confirmReservation(reservation);
        reservations.add(reservation);
    }
    void cancelReservation(Reservation reservation) {
        // Cancel Payment
        payment.cancelReservation(reservation);
        reservation.cancelReservation();
        reservations.remove(reservation);
    }

    // Dont Know The Logic
    void updateReservation(Reservation reservation) {
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
