package com.rangel.connections;

import java.io.Closeable;
import java.io.IOException;

public interface Connection extends Closeable {
    
    String sendPix(int value, String key) throws IOException;
}
