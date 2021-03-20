package com.fournoobs;

import java.io.IOException;

import krpc.client.RPCException;
import krpc.client.StreamException;

public interface Launchable {
    
    /**
     * Represents the main update loop of a vehicle guidance program
     * 
     * @return false if flight is over, else true
     * @throws IOException
     * @throws StreamException
     * @throws RPCException
     */
    public boolean update() throws RPCException, StreamException, IOException;
}
