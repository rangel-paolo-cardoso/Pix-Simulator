package com.rangel.pix;

import java.io.IOException;

import com.rangel.connections.Connection;
import com.rangel.exceptions.BlankKeyError;
import com.rangel.exceptions.InsufficientBalanceError;
import com.rangel.exceptions.InternalError;
import com.rangel.exceptions.KeyNotFoundError;
import com.rangel.exceptions.NoPositiveValueError;
import com.rangel.exceptions.PixError;
import com.rangel.messages.Messages;
import com.rangel.messages.ReturnCodes;
import com.rangel.server.Server;

public class PixProcessor {
    
    private final Server server;

    public PixProcessor(Server server) {
        this.server = server;
    }

    public void executePix(int value, String key) throws PixError, IOException {
        if (value <= 0) {
            throw new NoPositiveValueError(Messages.NO_POSITIVE_VALUE);
        }
        if (key.trim().equals("")) {
            throw new BlankKeyError(Messages.BLANK_KEY);
        }

        String response = null;
        try (Connection connection = server.openConnection()) {
            response = connection.sendPix(value, key);
        }

        switch (response) {
            case ReturnCodes.SUCCESS:
                break;
            case ReturnCodes.INSUFFICIENT_BALANCE:
                throw new InsufficientBalanceError(Messages.INSUFFICIENT_BALANCE);
            case ReturnCodes.PIX_KEY_NOT_FOUND:
                throw new KeyNotFoundError(Messages.KEY_NOT_FOUND);
            default:
                throw new InternalError(Messages.INTERNAL_ERROR);
        }
    }
}
