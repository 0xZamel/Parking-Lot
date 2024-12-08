package com.example.parking.spot;
import com.example.parking.*;
import java.util.*;

public abstract class Spot {
    protected int spotID;
    protected double hourRate;
    protected Map<Integer, Slot> slots;

    protected static final double  BIKE_HOUR_RATE = 3, CAR_HOUR_RATE = 5, TRUCK_HOUR_RATE = 7;

    public Spot(int spotID, double hourRate) {
        this.spotID = spotID;
        this.hourRate = hourRate;
    }

    public int getSpotID() {
        return spotID;
    }
    public double getHourRate() {
        return hourRate;
    }

    public abstract boolean isSuitableFor(VehicleType vehicleType);
    public abstract VehicleType getSpotType();

    public Map<Integer, Slot> getAllSlots() {
        return slots;
    }

    public boolean isSlotExists(int slotID) {
        return slots.containsKey(slotID);
    }
    public void addSlot(Slot slot) {
        slots.put(slot.getSlotID(), slot);
    }
    public void removeSlot(int slotID) {
       slots.remove(slotID);
    }

    public Slot getSlot(int slotID) {
        return slots.get(slotID);
    }
}

