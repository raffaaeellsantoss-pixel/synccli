package com.synccli.config;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@Component
public class RawWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            String text = message.getPayload();

            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            System.out.println("Copiado direto: " + text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}