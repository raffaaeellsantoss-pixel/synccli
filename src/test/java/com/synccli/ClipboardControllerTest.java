package com.synccli.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClipboardControllerTest {

    @Test
    void shouldReturnSameMessage() {
        ClipboardController controller = new ClipboardController();

        String message = "teste comando";
        String result = controller.send(message);

        assertEquals(message, result);
    }
}