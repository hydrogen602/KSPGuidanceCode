package com.fournoobs;


public class Main {

    private static final int[] heights = {50, 100, 200, 400, 800};

    public static void main(String[] args) {
	// write your code here
        System.out.println("test");
        try {
            Connecting c = new Connecting("Suborbital Hop", "localhost", 50000, 50001);
            c.getVessel().getControl().activateNextStage();
            c.getSpaceCenter().quicksave();

            for (int h: heights) {
                c.getSpaceCenter().quickload();
                HopperMK1 mk1 = new HopperMK1(c.getVessel(), c.getConn());
                mk1.shutoffHeight = h;
                System.out.println("Aiming for " + h);

                while (mk1.update()) {}

                Thread.sleep(5000);
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
