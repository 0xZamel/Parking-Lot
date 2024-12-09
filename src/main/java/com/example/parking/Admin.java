package com.example.parking;
import com.example.parking.spot.*;

public class Admin extends Person {
    public Admin(String userName, String password) {
        super(userName, password);
    }

    public void addSlot(int spotID, Slot slot) {
        SystemManager.allSpots.get(spotID).addSlot(slot);
    }
    public void removeOwner(int ownerID) {
        SystemManager.allOwners.remove(ownerID);
    }
    public void removeSpot(int spotID) {
        SystemManager.allSpots.remove(spotID);
    }
    public void removeSlot(int slotID) {
        for (Spot spot : SystemManager.allSpots.values()) {
            if (spot.isSlotExists(slotID)) {
                spot.removeSlot(slotID);
                break;
            }
        }
    }

    public double calculateTotalAmount(VehicleType vehicleType)
    {
        double totalAmount = 0;
        for (Reservation reservation : SystemManager.allReservations.values()) {
            if (reservation.isActive()  &&
                SystemManager.allSpots.get(reservation.getSpotID()).getSpotType() == vehicleType)
            {
                totalAmount += reservation.getTotalAmount();
            }
        }
        return totalAmount;
    }
}
