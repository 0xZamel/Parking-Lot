package com.example.parking;
import java.time.LocalDateTime;
import java.time.Duration;

public class Slot {
    private final int slotID, spotID;
    private LocalDateTime startDate, endDate; // 2024-12-07T14:30:00 --> yyyy-MM-dd'T'HH:mm:ss
    private final int hours;
    private boolean isAvailable;


    public Slot(int slotID , int spotID, LocalDateTime startDate, LocalDateTime endDate) {
        this.slotID = slotID;
        this.spotID = spotID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = (int)Duration.between(startDate,endDate).toHours();
        this.isAvailable = true;
    }


    public void bookSlot() {
        isAvailable = false;
    }
    public void cancelBooking() {
        isAvailable = true;
    }

    public int getSlotID() {
        return slotID;
    }
    public int getSpotID() {
        return spotID;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getHours() {
        return hours;
    }
    public boolean isAvailable() { return isAvailable; }
}