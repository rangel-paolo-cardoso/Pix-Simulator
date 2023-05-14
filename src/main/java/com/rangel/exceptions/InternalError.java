package com.rangel.exceptions;

import com.rangel.messages.Messages;

public class InternalError extends PixError {

    public InternalError() {
        super(Messages.INTERNAL_ERROR);
    }

    public InternalError(String message) {
        super(message);
    }
}
