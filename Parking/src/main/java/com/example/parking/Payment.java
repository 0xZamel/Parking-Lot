package com.example.parking;
import com.example.parking.spot.Spot;
import java.util.*;

public class Payment {
    private final int ownerID;
    private double balance, penalty;
    // Example data: total hours for each VehicleType
    EnumMap<VehicleType, Integer> totalHours;

    private final int REWARD_HOURS = 7; // Every 7 Hours Get 1 Hour Free
    private final double PENALTY_AMOUNT = 3; // Three Dollars

    public Payment(int ownerID, double balance) {
        this.ownerID = ownerID;
        this.balance = balance;

        this.penalty = 0;
        totalHours = new EnumMap<>(VehicleType.class);
        for (VehicleType type : VehicleType.values()) {
            totalHours.put(type, 0);
        }
    }

    public double getBalance() {
        return balance;
    }
    public void deposit(double balance) {
        this.balance += balance;
    }
    public void withdraw(double balance) {
        this.balance -= balance;
    }

    public int getTotalHours(VehicleType vehicleType) {
        return totalHours.get(vehicleType);
    }
    public void setTotalHours(VehicleType vehicleType, int hours) {
        this.totalHours.put(vehicleType, hours);
    }

    public double getPenalty() {
        return penalty;
    }
    public void addPenalty(double penalty) {
        this.penalty += penalty;
    }
    public void resetPenalty() {
        this.penalty = 0;
    }

    private double calc_fees(int hours, double hour_rate) {
        return hours * hour_rate;
    }
    public double reservationBaseAmount(int spotID, int slotID) {
        Spot spot = SystemManager.getSpot(spotID);
        Slot slot = spot.getSlot(slotID);

        double amount = calc_fees(slot.getHours(), spot.getHourRate());
        int free_hours = (int)((totalHours.get(spot.getSpotType()) + slot.getHours()) / REWARD_HOURS);
        return (amount - calc_fees(free_hours, spot.getHourRate()));
    }
    public double reservationTotalAmount(int spotID, int slotID)  {
        return reservationBaseAmount(spotID, slotID) + getPenalty();
    }

    public void confirmReservation(Reservation reservation) {
        Spot spot = SystemManager.getSpot(reservation.getSpotID());
        Slot slot = reservation.getSlot();

        double amount = reservation.getTotalAmount();

        // Update Data
        int lastHours = (getTotalHours(spot.getSpotType()) + slot.getHours()) % REWARD_HOURS;
        setTotalHours(spot.getSpotType(), lastHours);
        withdraw(amount);
        resetPenalty();
    }
    public void cancelReservation(Reservation reservation) {
        Spot spot = SystemManager.getSpot(reservation.getSpotID());
        Slot slot = reservation.getSlot();

        int lastHours = ((getTotalHours(spot.getSpotType()) % REWARD_HOURS) - (slot.getHours() % REWARD_HOURS) + REWARD_HOURS) % REWARD_HOURS;
        setTotalHours(spot.getSpotType(), lastHours);

        deposit(reservation.getBaseAmount());
        addPenalty(PENALTY_AMOUNT);
    }
}
