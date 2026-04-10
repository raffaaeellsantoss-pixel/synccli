package com.synccli.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RawWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = getSessionId(session);
        sessions.put(sessionId, session);
        System.out.println("Conectado: " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = getSessionId(session);
        String text = message.getPayload();

        System.out.println("Recebido de " + sessionId + ": " + text);

        // envia para o próprio usuário (outros dispositivos com mesmo ID)
        WebSocketSession target = sessions.get(sessionId);

        if (target != null && target.isOpen()) {
            target.sendMessage(new TextMessage(text));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = getSessionId(session);
        sessions.remove(sessionId);
        System.out.println("Desconectado: " + sessionId);
    }

    private String getSessionId(WebSocketSession session) {
        String query = session.getUri().getQuery(); // sessionId=xxx
        return query.split("=")[1];
    }
}