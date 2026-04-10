package com.synccli.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebSocketConfigTest {

    @Test
    void shouldInstantiateConfig() {
        WebSocketConfig config = new WebSocketConfig();
        assertNotNull(config);
    }
}