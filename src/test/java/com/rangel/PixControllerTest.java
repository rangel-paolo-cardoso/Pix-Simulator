package com.rangel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rangel.exceptions.BlankKeyError;
import com.rangel.exceptions.InsufficientBalanceError;
import com.rangel.exceptions.InternalError;
import com.rangel.exceptions.KeyNotFoundError;
import com.rangel.exceptions.NoPositiveValueError;
import com.rangel.exceptions.PixError;
import com.rangel.messages.Messages;
import com.rangel.pix.PixController;
import com.rangel.pix.PixProcessor;

public class PixControllerTest {
    
    private PixProcessor pixProcessor;
    private PixController pixController;

    @BeforeEach
    public void before() {
        pixProcessor = mock(PixProcessor.class);
        pixController = new PixController(pixProcessor);
    }

    @Test
    @DisplayName("Success message must be displayed correctly")
    public void successMessage() {
        String message = pixController.whenConfirmingPix(2000, "abc123");
        assertEquals(Messages.SUCCESS, message);
    }

    @Test
    @DisplayName("Message of a non-positive value must be displayed correctly")
    public void nonPositiceValueMessage() throws PixError, IOException {
        doThrow(new NoPositiveValueError()).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(0, "abc123");
        assertEquals(Messages.NO_POSITIVE_VALUE, message);
    }

    @Test
    @DisplayName("Message of blank key must be displayed correctly")
    public void blankKeyMessage() throws PixError, IOException {
        doThrow(new BlankKeyError()).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(2000, " ");
        assertEquals(Messages.BLANK_KEY, message);
    }

    @Test
    @DisplayName("Message of insufficient balance must be displayed correctly")
    public void insufficientBalanceMessage() throws PixError, IOException {
        doThrow(new InsufficientBalanceError()).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(1000000, "abc123");
        assertEquals(Messages.INSUFFICIENT_BALANCE, message);
    }

    @Test
    @DisplayName("Message of key not found must be displayed correctly")
    public void keyNotFoundMessage() throws PixError, IOException {
        doThrow(new KeyNotFoundError()).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(2000, "Pix key not found.");
        assertEquals(Messages.KEY_NOT_FOUND, message);
    }

    @Test
    @DisplayName("Message of internal error must be displayed correctly")
    public void internalErrorMessage() throws PixError, IOException {
        doThrow(new InternalError()).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(2000, "abc123");
        assertEquals(Messages.INTERNAL_ERROR, message);
    }

    @Test
    @DisplayName("Message of connection error must be displayed correctly")
    public void connectionErrorMessage() throws PixError, IOException {
        doThrow(SocketTimeoutException.class).when(pixProcessor).executePix(anyInt(), anyString());
        String message = pixController.whenConfirmingPix(2000, "abc123");
        assertEquals(Messages.CONNECTION_ERROR, message);
    }
}
