package com.fournoobs;

import krpc.client.Connection;
import krpc.client.RPCException;
// import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;

import java.io.IOException;

public class Connecting {

    private final Connection conn;
    //private final KRPC krpc;
    private final SpaceCenter spaceCenter;
    private final SpaceCenter.Vessel vessel;

    public Connecting(String name, String host, int rpcPort, int streamPort) throws IOException, RPCException {
        conn = Connection.newInstance(name, host, rpcPort, streamPort);
        //krpc = KRPC.newInstance(conn);
        spaceCenter = SpaceCenter.newInstance(conn);
        vessel = spaceCenter.getActiveVessel();
        System.out.println(vessel.getName());
    }

    public SpaceCenter.Vessel getVessel() {
        return vessel;
    }

    public SpaceCenter getSpaceCenter() {
        return spaceCenter;
    }

    public Connection getConn() {
        return conn;
    }

    //    public static void main() throws IOException, RPCException {
//        try (Connection connection = Connection.newInstance("Suborbital Test", "localhost", 50000, 50001)) {
//            KRPC k = KRPC.newInstance(connection);
//            SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
//            SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
//            System.out.println(vessel.getName());
//            System.out.println(k.getStatus().getVersion());
//        }
//    }
}