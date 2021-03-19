package com.fournoobs;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.StreamException;

public class Main {

    private static final int[] heights = {50, 100, 200, 400, 800};

    public static void main(String[] args) {
	// write your code here
        System.out.println("test");
        try {
            Connecting c = new Connecting("Suborbital Hop", "localhost", 50000, 50001);
            c.getVessel().getControl().activateNextStage();
            
            oneHop(c);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }

    public static void oneHop(Connecting conn) throws RPCException, IOException, StreamException {
        HopperMK1 mk1 = new HopperMK1(conn.getVessel(), conn.getConn());
        while (mk1.update()) {}
    }

    public static void hopCampaign(Connecting conn) throws RPCException, IOException, StreamException {
        conn.getSpaceCenter().quicksave();

        for (int h: heights) {
            conn.getSpaceCenter().quickload();
            HopperMK1 mk1;
                mk1 = new HopperMK1(conn.getVessel(), conn.getConn());
            
            mk1.shutoffHeight = h;
            System.out.println("Aiming for " + h);

            while (mk1.update()) {}

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
