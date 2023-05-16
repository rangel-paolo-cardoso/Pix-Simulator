package com.rangel.server;

import java.io.IOException;

import com.rangel.connections.Connection;
import com.rangel.messages.ReturnCodes;

public class FakeServer implements Server {

    @Override
    public Connection openConnection() throws IOException {
        return new Connection() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public String sendPix(int value, String key) throws IOException {
                return ReturnCodes.SUCCESS;
            }
        };
    }
    
}
