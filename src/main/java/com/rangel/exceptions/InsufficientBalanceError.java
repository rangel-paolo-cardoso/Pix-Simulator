package com.rangel.exceptions;

import com.rangel.messages.Messages;

public class InsufficientBalanceError extends PixError {

    public InsufficientBalanceError() {
        super(Messages.INSUFFICIENT_BALANCE);
    }

    public InsufficientBalanceError(String message) {
        super(message);
    }
}
