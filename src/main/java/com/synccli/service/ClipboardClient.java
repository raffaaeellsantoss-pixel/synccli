package com.synccli;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ClipboardClient extends WebSocketClient {

    public ClipboardClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conectado ao /raw");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Recebido: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Conexão fechada");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        ClipboardClient client = new ClipboardClient(
                new URI("ws://localhost:8080/raw")
        );

        client.connect();

        Thread.sleep(Long.MAX_VALUE);
    }
}