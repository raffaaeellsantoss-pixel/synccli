package com.synccli;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;

public class ClipboardClient extends WebSocketClient {

    public ClipboardClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conectado");

        send("CONNECT\naccept-version:1.2\nhost:localhost\n\n\u0000");

        send("SUBSCRIBE\nid:sub-0\ndestination:/topic/clipboard\n\n\u0000");
    }

    @Override
    public void onMessage(String message) {
        // STOMP manda headers + body → precisamos pegar só o corpo
        if (message.contains("\n\n")) {
            String body = message.split("\n\n")[1].replace("\u0000", "");

            try {
                StringSelection selection = new StringSelection(body);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                System.out.println("Copiado: " + body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                new URI("ws://localhost:8080/ws/websocket")
        );

        client.connect();

        Thread.sleep(Long.MAX_VALUE);
    }
}