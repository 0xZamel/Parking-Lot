package com.example.parking;
import java.util.regex.Pattern;

class Vehicle {
    private VehicleType Type;
    private String licensePlate;
    private String brand;
    public Vehicle(VehicleType Type, String licensePlate, String brand) {
        this.Type = Type;
        this.licensePlate = licensePlate;
        this.brand = brand;
    }


    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public VehicleType getType() {
        return Type;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public static boolean isValidLicensePlate(String plate) {
        // Example  1-3 letters, dash, 1-4 digits
        String regex = "^[A-Z]{1,3}-\\d{1,4}$";
        return Pattern.matches(regex, plate);
    }
}