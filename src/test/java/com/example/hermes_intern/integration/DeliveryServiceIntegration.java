package com.example.hermes_intern.integration;

import com.example.hermes_intern.domain.Delivery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void getCourierDeliveries()
            throws Exception {
        this.webClient.get().uri("/deliveries/oncourier").exchange().expectStatus().isOk()
                .expectBodyList(Delivery.class);
    }
}