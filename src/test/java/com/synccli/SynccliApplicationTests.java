package com.synccli;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SyncCliApplicationTests {

	@Test
	void contextLoads() {
		// se subir sem erro, já passou
	}
	@Test
	void shouldSendMessageThroughController() {
		ClipboardController controller = new ClipboardController();

		String input = "ipconfig";
		String output = controller.send(input);

		assertEquals(input, output);
	}
	@SpringBootTest
	class IntegrationTest {

		@Test
		void contextLoads() {
		}
	}
}