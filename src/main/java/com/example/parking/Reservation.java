package com.example.parking;
import java.time.LocalDateTime;

public class Reservation {
    private final int reservationID, ownerID, baseAmount, totalAmount;
    private final Slot slot;
    private final LocalDateTime reservationDate; // 2024-12-07T14:30:00 --> yyyy-MM-dd'T'HH:mm:ss
    private LocalDateTime cancelReservationDate;
    private boolean status;

    public Reservation(int reservationID, int ownerID, Slot slot, int baseAmount, int totalAmount) {
        this.reservationID = reservationID;
        this.ownerID = ownerID;
        this.slot = slot;
        this.status = true;
        this.reservationDate = LocalDateTime.now();
        this.baseAmount = baseAmount;
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }
    public int getOwnerID() {
        return ownerID;
    }

    public int getReservationID() {
        return reservationID;
    }
    public int getBaseAmount() {
        return baseAmount;
    }
    public int getTotalAmount() {
        return totalAmount;
    }
    public int getSpotID() {
        return slot.getSpotID();
    }
    public int getHours() {
        return slot.getHours();
    }
    public Slot getSlot() {
        return slot;
    }

    public boolean isActive() {
        return status;
    }
    public void cancelReservation() {
        this.cancelReservationDate = LocalDateTime.now();
        this.status = false;
    }
}