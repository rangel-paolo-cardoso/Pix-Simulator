package com.rangel.pix;

import java.io.IOException;

import com.rangel.exceptions.PixError;
import com.rangel.messages.Messages;

public class PixController {
    
    private final PixProcessor pixProcessor;

    public PixController(PixProcessor pixProcessor) {
        this.pixProcessor = pixProcessor;
    }

    public String whenConfirmingPix(int value, String key) {
        try {
            pixProcessor.executePix(value, key);
        } catch (PixError e) {
            return e.getMessage();
        } catch (IOException e) {
            return Messages.CONNECTION_ERROR;
        }
        return Messages.SUCCESS;
    }
}
