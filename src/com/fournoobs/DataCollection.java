package com.fournoobs;

import java.io.FileWriter;
import java.io.IOException;

public class DataCollection {
    /**
     * Write a json list to file
     */

    private FileWriter writer;
    private boolean firstEntry = true;

    public static final int entriesPerSec = 20;
    private static final double pause = 1000.0 / entriesPerSec;
    
    private long lastEntry = 0;
    private long startTime;

    public DataCollection(String filename) throws IOException {
        writer = new FileWriter(filename);
        writer.write("[\n");
        startTime = System.currentTimeMillis();
    }

    public void addEntry(double a, double b) throws IOException {
        long curTime = System.currentTimeMillis();
        if (curTime - lastEntry < pause) {
            return;
        }
        lastEntry = curTime;

        if (firstEntry) {
            firstEntry = false;
        }
        else {
            writer.write(",\n");
        }
        writer.write("  [" + (curTime - startTime) + ", " + a + ", " + b + "]");
    }

    public void close() {
        try {
            writer.write("\n]");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
