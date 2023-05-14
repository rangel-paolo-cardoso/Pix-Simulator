package com.rangel.server;

import java.io.IOException;

import com.rangel.connections.Connection;

public interface Server {
    
    /**
     * This method is used to open a connection to start the banking operation.
     * @return
     * @throws IOException
     */
    Connection openConnection() throws IOException;
}
