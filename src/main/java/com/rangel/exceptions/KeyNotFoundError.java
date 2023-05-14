package com.rangel.exceptions;

import com.rangel.messages.Messages;

public class KeyNotFoundError extends PixError {

    public KeyNotFoundError() {
        super(Messages.KEY_NOT_FOUND);
    }

    public KeyNotFoundError(String message) {
        super(message);
    }
}
