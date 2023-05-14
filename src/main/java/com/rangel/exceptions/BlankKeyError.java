package com.rangel.exceptions;

import com.rangel.messages.Messages;

public class BlankKeyError extends PixError {

    public BlankKeyError() {
        super(Messages.BLANK_KEY);
    }

    public BlankKeyError(String message) {
        super(message);
    }
}
