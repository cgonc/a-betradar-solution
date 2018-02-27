package com.iceland.betradar.service;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * A singleton iceland socket which represents our socket server.
 */
public enum IcelandSocket {
    INSTANCE;
    private Socket betradarClientRoom;
    private String secretKey = "alxlhj123%&4+###klmfkjhgnbv4rf94ds59hj!";

    IcelandSocket() {
        try {
            this.betradarClientRoom = IO.socket("http://localhost:8880/betRadarClientRoom");
            this.betradarClientRoom.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("FATAL ERROR ICELAND SOCKET CAN NOT BE CREATED.");
        }
    }

    public void emitMessageToBetRadarClientRoom(String channel, String message) {
        this.betradarClientRoom.emit(channel + secretKey,message);
    }

}
