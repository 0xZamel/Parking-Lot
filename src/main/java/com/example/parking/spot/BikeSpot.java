package com.example.parking.spot;

import com.example.parking.VehicleType;

public class BikeSpot extends Spot{
    public  BikeSpot(int id, double hourRate){
        super(id, BIKE_HOUR_RATE);
    }

    @Override
    public boolean isSuitableFor(VehicleType vehicleType){
        return vehicleType == VehicleType.Bike;
    }
    public VehicleType getSpotType() {
        return VehicleType.Bike;
    }
}