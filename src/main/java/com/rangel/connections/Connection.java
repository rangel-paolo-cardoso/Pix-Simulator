package com.rangel.connections;

import java.io.Closeable;
import java.io.IOException;

public interface Connection extends Closeable {
    
    /**
     * Method responsible for sending the Pix operation.
     * @param value
     * @param key
     * @return
     * @throws IOException
     */
    String sendPix(int value, String key) throws IOException;
}
