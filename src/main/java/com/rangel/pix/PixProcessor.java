package com.rangel.pix;

import java.io.IOException;

import com.rangel.exceptions.PixError;
import com.rangel.server.Server;

public class PixProcessor {
    
    private final Server server;

    public PixProcessor(Server server) {
        this.server = server;
    }

    public void executePix(int value, String key) throws PixError, IOException {
        // 
    }
}
