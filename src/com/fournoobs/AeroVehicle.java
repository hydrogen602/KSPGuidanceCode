package com.fournoobs;

import org.javatuples.Triplet;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter.Flight;
import krpc.client.services.SpaceCenter.Vessel;

public class AeroVehicle extends Vehicle {

    private double mu = 0;

    private Stream<Double> altitudeStream;
    private Stream<Double> verticalVelStream;
    private Stream<Triplet<Double, Double, Double>> dragStream;

    public AeroVehicle(Vessel v, Connection con) throws RPCException {
        super(v);

        Flight surfaceFlightInfo = v.flight(v.getOrbit().getBody().getReferenceFrame());
        Flight flightInfo = v.flight(v.getReferenceFrame());

        try {
            altitudeStream = con.addStream(flightInfo, "getSurfaceAltitude");
            verticalVelStream = con.addStream(surfaceFlightInfo, "getVerticalSpeed");
            dragStream = con.addStream(flightInfo, "getDrag");
        } catch (StreamException e) {
            e.printStackTrace();
        }

        //out = new DataCollection("flight.json");
    }

    @Override
    public void dataUpdate() throws RPCException, StreamException {
        super.dataUpdate();

        Triplet<Double, Double, Double> drag = dragStream.get();
        double dragScalar = Math.sqrt(Util.dotProduct(drag, drag));
        double v = verticalVelStream.get();
        mu = dragScalar / (getAirDensity(altitudeStream.get()) * v * v);
    }

    public static double getAirDensity(double h) {
        return 1.225 * Math.exp(-0.000139 * h);
    }

    public double suicideBurnHeight(double vTarget) {
        vTarget = Math.abs(vTarget);
        double dt = -0.1; // we have to move backwards through time
        double x = 0;
        double v = 0;
        
        double acceleration;
        while (v < vTarget) {
            if (Math.abs(v) > vTarget || x < 0) {
                throw new RuntimeException("yeet: v=" + v + " x=" + x);
            }
            acceleration = thrust / mass + g + mu * getAirDensity(x) * v * v / mass;

            x += v * dt;
            v += acceleration * dt;
        }

        return x;
    }
    
}
