package com.rangel.exceptions;

import com.rangel.messages.Messages;

public class NoPositiveValueError extends PixError {

    public NoPositiveValueError() {
        super(Messages.NO_POSITIVE_VALUE);
    }

    public NoPositiveValueError(String message) {
        super(message);
    }
}
