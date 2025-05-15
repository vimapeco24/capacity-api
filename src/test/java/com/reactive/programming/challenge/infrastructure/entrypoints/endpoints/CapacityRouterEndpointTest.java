package com.reactive.programming.challenge.infrastructure.entrypoints.endpoints;

import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.infrastructure.entrypoints.dto.CapacityResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Opcional, limpia contexto antes
class CapacityRouterEndpointTest {

    @Autowired
    private WebTestClient webTestClient;

    private Capacity testCapacity;

    @BeforeAll
    void setUp() {
        testCapacity = new Capacity(
                "Spring Security", // Cambia el nombre aquí
                "Framework que facilita la creación de aplicaciones distribuidas y microservicios.",
                List.of(1L, 2L)
        );
    }


    @Test
    @Order(1)
    void shouldCreateCapacitySuccessfully() {
        webTestClient.post()
                .uri("/capacities/api/capacity")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(testCapacity))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body, "La respuesta no debe ser nula");
                    assertEquals(TechnicalMessage.CAPACITY_CREATED.getMessage(), body);
                });
    }

    @Test
    @Order(2)
    void shouldReturnListOfCapacities() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capacities/api/capacity")
                        .queryParam("sortBy", "name")
                        .queryParam("direction", "asc")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapacityResponse.class)
                .consumeWith(response -> {
                    List<CapacityResponse> capacities = response.getResponseBody();
                    assertNotNull(capacities, "La respuesta no debe ser nula");
                    assertFalse(capacities.isEmpty(), "La lista de capacidades no debe estar vacía");
                    assertTrue(capacities.stream()
                                    .anyMatch(cap -> "Desarrollador Backend".equals(cap.getName())),
                            "Debe contener la capacidad 'Desarrollador Backend'");
                });
    }
}
