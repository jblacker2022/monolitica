package com.monolito.monolitica;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonoliticaApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		// Verifica que el contexto de la aplicación se carga correctamente.
	}

	@Test
	void testRegisterUser() {
		// Simula el registro de un usuario
		String registerUrl = "/register";
		String payload = "{\"username\":\"testuser\",\"password\":\"password123\"}";

		// Enviamos un POST request para registrar al usuario
		ResponseEntity<String> response = restTemplate.postForEntity(registerUrl, payload, String.class);

		// Verificamos que la respuesta tiene un código de estado 200 (OK)
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("User registered successfully"));
	}

	@Test
	void testLoginUser() {
		// Simula el login de un usuario
		String loginUrl = "/login";
		String payload = "{\"username\":\"testuser\",\"password\":\"password123\"}";

		// Enviamos un POST request para loguearnos
		ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, payload, String.class);

		// Verificamos que la respuesta tiene un código de estado 200 (OK)
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("Welcome testuser"));
	}

	@Test
	void testInvalidLogin() {
		// Intentamos loguearnos con credenciales incorrectas
		String loginUrl = "/login";
		String payload = "{\"username\":\"testuser\",\"password\":\"wrongpassword\"}";

		// Enviamos un POST request para loguearnos
		ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, payload, String.class);

		// Verificamos que la respuesta tiene un código de estado 401 (Unauthorized)
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertTrue(response.getBody().contains("Invalid credentials"));
	}
}
