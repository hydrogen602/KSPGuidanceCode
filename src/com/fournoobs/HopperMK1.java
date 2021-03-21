package com.fournoobs;

import java.io.IOException;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter.Vessel;


public class HopperMK1 extends FlightVehicle implements Launchable {

    public double shutoffHeight = 50;

    private double apogee = 0;

    private DataCollection out;

    public HopperMK1(Vessel v, Connection con) throws RPCException, IOException {
        super(v, con);

        out = new DataCollection("flight.json");
    }

//    public void run() throws RPCException, StreamException {
//        while (true) {
//            System.out.println(altitudeStream.get());
//        }
//    }

    public boolean update() throws RPCException, StreamException, IOException {
        dataUpdate();

        double altitude = altitudeStream.get();
        double vertical_vel = verticalVelStream.get();

        out.addEntry(altitude, vertical_vel);

        if (altitude > apogee) {
            apogee = altitude;
        }
        double sBurnHeight;

        switch (phase) {
            case PRE_LAUNCH:
                vessel.getAutoPilot().targetPitchAndHeading(90, 90);
                vessel.getAutoPilot().engage();
                vessel.getControl().setThrottle(1);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Launch");
                phase = Phase.ASCENT;
                break;

            case ASCENT:
                if (altitude > shutoffHeight) {
                    System.out.println("Altitude Reached");
                    vessel.getControl().setThrottle(0);
                    phase = Phase.DESCENT;
                }
                break;

            case DESCENT:
                sBurnHeight = suicideBurnHeight(vertical_vel) + 15;
                if (vertical_vel < 0 && sBurnHeight >= altitude) {
                    vessel.getControl().setThrottle(throttle_for_calculation);
                    System.out.println("Suicide burn time");
                    System.out.println(sBurnHeight + " " + altitude);
                    phase = Phase.SUICIDE_BURN;
                }
                break;

            case SUICIDE_BURN:
                sBurnHeight = suicideBurnHeight(vertical_vel);
                System.out.print("\r" + sBurnHeight + " " + altitude + " " + mass);
                if (sBurnHeight + 5 > altitude) {
                    vessel.getControl().setThrottle(1);
                }
                else if (sBurnHeight < altitude - 50) {
                    vessel.getControl().setThrottle(throttle_for_calculation / 2.0f);
                }
                else {
                    vessel.getControl().setThrottle(throttle_for_calculation);
                }

                if (altitude < 3) {
                    System.out.println();
                    vessel.getControl().setThrottle(0);
                    System.out.println("Landed");
                    System.out.println("vel = " + vertical_vel + ", alt = " + altitude);
                    phase = Phase.LANDED;
                }
                break;

            case LANDED:
                System.out.println("Apogee = " + apogee);
                out.close();
                return false;
        }

        return true;
    }
}
