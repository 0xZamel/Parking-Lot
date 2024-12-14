package com.example.parking;
import com.example.parking.spot.*;

import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.max;

public class SystemManager {
    public static Map<Integer, Owner> allOwners;
    public static Map<Integer, Spot> allSpots;
    public static Map<Integer, Reservation> allReservations;
    public static  ArrayList<Feedback> feedbacksList;
    public static int nextOwnerID, nextSpotID, nextReservationID, nextSlotID;

    public static void initialize() {
        allSpots = new HashMap<>();
        allOwners = new HashMap<>();
        allReservations = new HashMap<>();
        feedbacksList = new ArrayList<>();
//
//        Spot spot = new CarSpot(10);
//        Spot spot2 = new BikeSpot(12);
//
//        for (int i = 1; i < 20; ++i)
//            spot.addSlot(new Slot(i, 10, LocalDateTime.now(), LocalDateTime.now()));
//        spot2.addSlot(new Slot(15, 12, LocalDateTime.now(), LocalDateTime.now()));
//        allSpots.put(spot.getSpotID(), spot);
//        allSpots.put(spot2.getSpotID(), spot2);
//
//        ArrayList<Vehicle> vehicles = new ArrayList<>();
//        vehicles.add(new Vehicle(VehicleType.Car, "AD-456"));
//        Owner owner = new Owner("Fady", "123456", 1, "ASDA", vehicles, 1000);
//        allOwners.put(owner.getOwnerID(), owner);
//        owner = new Owner("Bousy", "123456", 3, "ASDA", vehicles, 1000);
//        allOwners.put(owner.getOwnerID(), owner);
//
//        Reservation res = new Reservation(1, 1, spot.getSlot(1),  10, 15);
//        allReservations.put(res.getReservationID(), res);
//        res = new Reservation(2, 3, spot2.getSlot(15),  100, 155);
//        allReservations.put(res.getReservationID(), res);

        setIDs();
    }
    // Set The Initial IDs
    public static void setIDs() {
        nextOwnerID = nextSpotID = nextReservationID = nextSlotID = 1;
        for (Owner owner : allOwners.values()) {
            nextOwnerID = max(nextOwnerID, owner.getOwnerID() + 1);
        }
        for (Spot spot : allSpots.values()) {
            nextSpotID = max(nextSpotID, spot.getSpotID() + 1);

            for (Slot slot : spot.getSlots()) {
                nextSlotID = max(nextSlotID, slot.getSlotID() + 1);
            }
        }
        for (Reservation reservation : allReservations.values()) {
            nextReservationID = max(nextReservationID, reservation.getReservationID() + 1);
        }
    }

    // Spots & Slots
    public static Spot getSpot(int spotId) {
        return allSpots.get(spotId);
    }
    public static ArrayList<Spot> getSpotsByType(VehicleType vehicleType) {
        ArrayList<Spot> spots = new ArrayList<>();
        for (Spot spot : allSpots.values()) {
            if (spot.getSpotType() == vehicleType) {
                spots.add(spot);
            }
        }
        return spots;
    }

    public static ArrayList<Slot> getSlotsBySpotID(int spotID) {
        return allSpots.get(spotID).getSlots();
    }

    // Get all slots from all spots
    public static ArrayList<Slot> getAllSlots() {
        ArrayList<Slot> allSlots = new ArrayList<>();
        for (Spot spot : allSpots.values()) {
            allSlots.addAll(spot.getSlots()); // assuming getSlots() returns a map
        }
        return allSlots;
    }
    // Get All Slots For Owner
    public static ArrayList<Slot> getAllAvaiableSlots(Owner owner) {
        Map<VehicleType, Boolean> currentVehicles = new HashMap<>();
        for (Vehicle vehicle : owner.getVehicles())
            currentVehicles.put(vehicle.getType(), true);

        ArrayList<Slot> slots = new ArrayList<>();
        for (Spot spot : allSpots.values()) {
            if (currentVehicles.containsKey(spot.getSpotType())) {
                for (Slot slot : spot.getSlots()) {
                    if (slot.isAvailable()) {
                        slots.add(slot);
                    }
                }
            }
        }
        return slots;
    }

    // Owners
    public static ArrayList<Owner> getOwners() {
        return new ArrayList<Owner>(allOwners.values());
    }
    public static Owner getOwner(String userName) {
        for (Owner owner : allOwners.values()) {
            if (owner.getUserName().equals(userName)) {
                return owner;
            }
        }
        return null;
    }

    public static boolean isOwnerExist(String userName, String password) {
        for (Owner owner : allOwners.values()) {
            if (userName.equals(owner.getUserName()) && password.equals(owner.getPassword()))
                return true;
        }
        return false;
    }
    public static boolean isOwnerExist(String userName) {
        for (Owner owner : allOwners.values()) {
            if (userName.equals(owner.getUserName()))
                return true;
        }
        return false;
    }

    public static void addOwner(String userName, String Password, String licenceNumber,
                                ArrayList<Vehicle> vehicles, double balance)
    {
        Owner newOwner = new Owner(userName, Password, nextOwnerID, licenceNumber, vehicles, balance);
        allOwners.put(newOwner.getOwnerID(), newOwner);
        nextOwnerID++;
    }

    // FeedBacks
    public static ArrayList<Feedback> getAllFeedBacks(){
        return feedbacksList;
    }

    // Reservations
    public static ArrayList<Reservation> getAllReservations(){
        return new ArrayList<Reservation>(allReservations.values());
    }
    public static ArrayList<Reservation> getReservationsWithType(VehicleType vehicleType) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : allReservations.values()) {
            if (reservation.getSpotType() == vehicleType) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }
    public static void addNewReservation(int ownerID, Slot slot, double baseAmount, double totalAmount) {
        Reservation reservation = new Reservation(nextReservationID, ownerID, slot, baseAmount, totalAmount);
        allReservations.put(reservation.getReservationID(), reservation);
        allOwners.get(ownerID).makeReservation(reservation);
        nextReservationID++;
    }

    // Files
    // Save all data to file
    public static void save_data_to_file() {
        ArrayList<CarSpot> carSpots = new ArrayList<>();
        ArrayList<BikeSpot> bikeSpots = new ArrayList<>();
        ArrayList<TruckSpot> truckSpots = new ArrayList<>();

        for (Spot spot : allSpots.values()) {
            if (spot instanceof CarSpot) {
                carSpots.add((CarSpot) spot);
            } else if (spot instanceof BikeSpot) {
                bikeSpots.add((BikeSpot) spot);
            } else if (spot instanceof TruckSpot) {
                truckSpots.add((TruckSpot) spot);
            }
        }

        // Save each type of spot
        CarSpot.saveSpots(carSpots);
        BikeSpot.saveSpots(bikeSpots);
        TruckSpot.saveSpots(truckSpots);

        // Save other components
        Owner.saveOwners(new ArrayList<>(allOwners.values()));
        Reservation.saveReservations(new ArrayList<>(allReservations.values()));
        Slot.saveSlots(new ArrayList<>(getAllSlots()));
        Feedback.saveFeedbackToFile();
    }


    public static void load_data_from_file() {
        // Load spots by type
        ArrayList<CarSpot> carSpots = CarSpot.loadSpots();
        ArrayList<BikeSpot> bikeSpots = BikeSpot.loadSpots();
        ArrayList<TruckSpot> truckSpots = TruckSpot.loadSpots();

        // Merge all spots into allSpots
        for (CarSpot carSpot : carSpots) {
            allSpots.put(carSpot.getSpotID(), carSpot);
        }
        for (BikeSpot bikeSpot : bikeSpots) {
            allSpots.put(bikeSpot.getSpotID(), bikeSpot);
        }
        for (TruckSpot truckSpot : truckSpots) {
            allSpots.put(truckSpot.getSpotID(), truckSpot);
        }

        // Load other components
        ArrayList<Owner> owners = Owner.loadOwners();
        ArrayList<Reservation> reservations = Reservation.loadReservations();

        for (Owner owner : owners) {
            allOwners.put(owner.getOwnerID(), owner);
        }
        for (Reservation reservation : reservations) {
            allReservations.put(reservation.getReservationID(), reservation);
        }

        // Reload feedbacks
        feedbacksList = Feedback.loadFeedbacks();
    }

}