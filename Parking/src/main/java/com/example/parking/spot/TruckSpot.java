package com.example.parking.spot;

import com.example.parking.VehicleType;

public class TruckSpot extends Spot{
    public TruckSpot (int id ,double hourRate){
        super(id,hourRate);
    }

    @Override
    public boolean isSuitableFor(VehicleType vehicleType){
        return vehicleType == VehicleType.Truck;
    }
    public VehicleType getSpotType() {
        return VehicleType.Truck;
    }

}