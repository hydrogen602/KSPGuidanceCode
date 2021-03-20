package com.fournoobs;

import krpc.client.RPCException;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter.Vessel;

public class Vehicle {

    protected static float throttle_for_calculation = 0.8f;

    protected double mass;
    protected double thrust;
    protected Vessel vessel;

    public static final double g = -9.8;  

    public Vehicle(Vessel v) throws RPCException {
        mass = v.getMass();
        thrust = v.getMaxThrust();
        vessel = v;
        System.out.println("Vehicle with mass=" + mass + " and thrust=" + thrust);
        //System.out.println(v.getName());
    }

    public double suicideBurnHeight(double vel) {
        return 3.0/2.0 * mass * vel * vel / (thrust * Vehicle.throttle_for_calculation - mass * g);
    }

    public void dataUpdate() throws RPCException, StreamException {
        mass = vessel.getMass();
    }

}
