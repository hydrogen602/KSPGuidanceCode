package com.fournoobs;

import java.io.IOException;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter.Flight;
import krpc.client.services.SpaceCenter.Vessel;


enum Phase {
    PRE_LAUNCH,
    ASCENT,
    DESCENT,
    SUICIDE_BURN,
    LANDED
}


public class FlightVehicle extends Vehicle {

    protected Stream<Double> altitudeStream;
    protected Stream<Double> verticalVelStream;

    protected Phase phase = Phase.PRE_LAUNCH;
    protected Flight flightInfo;
    protected Flight surfaceFlightInfo;

    //private DataCollection out;

    public FlightVehicle(Vessel v, Connection con) throws RPCException, IOException {
        super(v);
        surfaceFlightInfo = v.flight(v.getOrbit().getBody().getReferenceFrame());
        flightInfo = v.flight(v.getReferenceFrame());

        try {
            altitudeStream = con.addStream(flightInfo, "getSurfaceAltitude");
            verticalVelStream = con.addStream(surfaceFlightInfo, "getVerticalSpeed");
        } catch (StreamException e) {
            e.printStackTrace();
        }

        //out = new DataCollection("flight.json");
    }
    
    
}
