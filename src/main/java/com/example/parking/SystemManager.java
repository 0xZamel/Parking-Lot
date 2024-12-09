package com.example.parking;
import com.example.parking.spot.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static sun.swing.MenuItemLayoutHelper.max;

public class SystemManager {
    static Map<Integer, Spot> allSpots;
    static Map<Integer, Owner> allOwners;
    static Map<Integer, Reservation> allReservations;

    static int nextOwnerID, nextSpotID, nextReservationID, nextSlotID;

    public static void initialize() {
        allSpots = new HashMap<>();
        allOwners = new HashMap<>();
        allReservations = new HashMap<>();
        load_data_from_file();
        setIDs();
    }

    public static void setIDs() {
        nextOwnerID = nextSpotID = nextReservationID = nextSlotID = 1;
        for (Owner owner : allOwners.values()) {
            nextOwnerID = max(nextOwnerID, owner.getOwnerID() + 1);
        }
        for (Spot spot : allSpots.values()) {
            nextSpotID = max(nextSpotID, spot.getSpotID() + 1);

            for (Slot slot : spot.getAllSlots().values()) {
                nextSlotID = max(nextSlotID, slot.getSlotID() + 1);
            }
        }
        for (Reservation reservation : allReservations.values()) {
            nextReservationID = max(nextReservationID, reservation.getReservationID() + 1);
        }
    }
    public static  void load_data_from_file() {
    }
    public static void save_data_to_file() {
    }

    public static Spot getSpot(int spotId) {
        return allSpots.get(spotId);
    }
    public static boolean ownerLogin(String userName, String password)
    {
        for (Owner owner : allOwners.values()) {
            if (userName.equals(owner.getUserName()) && password.equals(owner.getPassword()))
                return true;
        }
        return false;
    }

    public static boolean isUserNameExist(String userName)
    {
        for (Owner owner : allOwners.values()) {
            if (userName.equals(owner.getUserName()))
                return true;
        }
        return false;
    }
    public static void register(String userName, String Password, String licenceNumber,
                                ArrayList<Vehicle> vehicles, double balance)
    {
        Owner newOwner = new Owner(userName, Password, nextOwnerID, licenceNumber, vehicles, balance);
        allOwners.put(newOwner.getOwnerID(), newOwner);
        nextOwnerID++;
    }


}
