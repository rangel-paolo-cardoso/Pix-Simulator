package com.rangel;

import com.rangel.pix.PixController;
import com.rangel.pix.PixProcessor;
import com.rangel.server.FakeServer;
import com.rangel.server.Server;

public class Main {

    /**
     * This main method is only used to validate the solution of the challenge.
     * 
     * @param args not used in this challenge.
     */
    public static void main(String[] args) {
        Server server = new FakeServer();
        PixProcessor pixProcessor = new PixProcessor(server);
        PixController pixController = new PixController(pixProcessor);
        String message = pixController.whenConfirmingPix(2000, "abc123");
        System.out.println(message);
    }
}
