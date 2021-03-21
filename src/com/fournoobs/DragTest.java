package com.fournoobs;

import java.io.IOException;

import org.javatuples.Triplet;

import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter.Vessel;

public class DragTest extends FlightVehicle implements Launchable {

    private DataCollection out;

    private Stream<Triplet<Double, Double, Double>> dragStream;

    public DragTest(Vessel v, Connection conn) throws RPCException, IOException, StreamException {
        super(v, conn);
        System.out.println("Drag Test");
        dragStream = conn.addStream(flightInfo, "getDrag");
        out = new DataCollection("flight.json", "Drag Test: " + vessel.getName());
    }

    @Override
    public boolean update() throws RPCException, StreamException, IOException {
        switch (phase) {
            case PRE_LAUNCH:
                phase = Phase.ASCENT;
                vessel.getAutoPilot().targetPitchAndHeading(90, 90);
                vessel.getAutoPilot().engage();
                vessel.getControl().setThrottle(1);
                break;
            case ASCENT:
                double drag = Util.mag(dragStream.get());
                double vel = verticalVelStream.get();
                double alt = altitudeStream.get();
                out.addEntry(alt, vel, drag);
                if (alt > 1500) {
                    phase = Phase.DESCENT;
                    vessel.getControl().setThrottle(0);
                    out.close();
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Unexpected Phase: " + phase);
        }
        return true;
    }
    
}
