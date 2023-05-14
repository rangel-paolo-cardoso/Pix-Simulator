package com.rangel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rangel.connections.Connection;
import com.rangel.exceptions.BlankKeyError;
import com.rangel.exceptions.InsufficientBalanceError;
import com.rangel.exceptions.InternalError;
import com.rangel.exceptions.KeyNotFoundError;
import com.rangel.exceptions.NoPositiveValueError;
import com.rangel.exceptions.PixError;
import com.rangel.messages.ReturnCodes;
import com.rangel.pix.PixProcessor;
import com.rangel.server.Server;

public class PixProcessorTest {

    private Server server;
    private Connection connection;
    private PixProcessor pixProcessor;

    @BeforeEach
    public void before() throws IOException {
        connection = mock(Connection.class);
        server = mock(Server.class);
        when(server.openConnection()).thenReturn(connection);
        pixProcessor = new PixProcessor(server);
    }

    @Test
    @DisplayName("Customized exception must be thrown when the value is less than or equal to zero")
    public void customizedExceptionValueLessThanOrEqualToZero() {
        assertThrows(
                NoPositiveValueError.class,
                () -> pixProcessor.executePix(0, "abc123"));
    }

    @Test
    @DisplayName("Customized exception must be thrown when the value is negative")
    public void customizedExceptionNegativeValue() {
        assertThrows(
                NoPositiveValueError.class,
                () -> pixProcessor.executePix(-1, "abc123"));
    }

    @Test
    @DisplayName("Customized exception must be thrown when the key is blank")
    public void customizedExceptionBlankKey() {
        assertThrows(
            BlankKeyError.class,
            () -> pixProcessor.executePix(2000, " ")
        );
    }

    @Test
    @DisplayName("Customized exception must be thrown when insufficient balance")
        public void customizedExceptionInsufficientBalnce() throws IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenReturn(ReturnCodes.INSUFFICIENT_BALANCE);

        assertThrows(
            InsufficientBalanceError.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );
    }

    @Test
    @DisplayName("Customized exception must be thrown when key not found")
    public void customizedExceptionKeyNotFound() throws IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenReturn(ReturnCodes.PIX_KEY_NOT_FOUND);

        assertThrows(
            KeyNotFoundError.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );
    }

    @Test
    @DisplayName("Customized exception must be thrown when internal error")
    public void customizedExceptionInternalError() throws IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenReturn("(Unknown returning code)");

        assertThrows(
            InternalError.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );
    }

    @Test
    @DisplayName("IOExceptions when opening connection must be propagated")
    public void ioExceptionWhenOpeningConnection() throws IOException {
        when(server.openConnection())
            .thenThrow(SocketTimeoutException.class);

        assertThrows(
            SocketTimeoutException.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );
    }

    @Test
    @DisplayName("IOExceptions when sending must be propagated")
    public void ioExceptionWhenSending() throws IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenThrow(SocketException.class);

        assertThrows(
            SocketException.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );
    }

    @Test
    @DisplayName("The connection must be closed after the operation is succeeded")
    public void conexaoFechadaAposSucesso() throws PixError, IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenReturn(ReturnCodes.SUCCESS);

        pixProcessor.executePix(2000, "abc123");
        verify(connection).close();
    }

    @Test
    @DisplayName("A conexão deve ser fechada mesmo em caso de erro")
    public void conexaoFechadaAposErro() throws IOException {
        when(connection.sendPix(anyInt(), anyString()))
            .thenThrow(SocketException.class);

        assertThrows(
            SocketException.class,
            () -> pixProcessor.executePix(2000, "abc123")
        );

        verify(connection).close();
    }
}
