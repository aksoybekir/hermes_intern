package com.example.hermes_intern.integration;

import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.DeliveryCheckOut;
import com.example.hermes_intern.model.DeliveryLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryServiceIntegration {

    @Autowired
    private WebTestClient webClient;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCourierDeliveries() throws Exception {

        this.webClient.get().uri("/deliveries/oncourier").exchange().expectStatus().isOk()
                .expectBodyList(Delivery.class);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "7e66464d-80e5-479d-811f-c2968dfedff6")
    public void getlocationbycustomer() throws Exception {

        this.webClient.get()
                .uri("/deliveries/customer/location/b2c24658-37d4-4d77-9cfc-cea77da59323")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    response.getResponseBody().equals("ON_WAY_WAREHOUSE");
        });
    }

    @Test
    @WithMockUser(roles = "BRANCH_WORKER")
    public void checkOutDelivery() throws Exception {
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deliveries/checkout")
                        .queryParam("id","b2c24658-37d4-4d77-9cfc-cea77da59323")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(DeliveryCheckOut.class);
    }
}