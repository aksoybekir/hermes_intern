package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.Actions;
import com.example.hermes_intern.domain.Customer;
import com.example.hermes_intern.domain.Delivery;
import com.example.hermes_intern.model.DeliveryCheckOut;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import com.example.hermes_intern.security.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "storage.bucket=HermesTest")
@AutoConfigureWebTestClient
public class DeliveryControllerIT {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ReactiveDeliveryRepository reactiveDeliveryRepository;

    Delivery testDelivery;

    @Before
    public void setUp() throws Exception {

        Customer testCustomer = new Customer();
        testCustomer.setId("7e66464d-80e5-479d-811f-c2968dfedff6");

        Actions testActions = new Actions();
        testActions.setDateCourierRecieved(new Date());

        testDelivery = new Delivery();
        testDelivery.setId("b2c24658-37d4-4d77-9cfc-cea77da59323");
        testDelivery.setCustomer(testCustomer);
        testDelivery.setActions(testActions);

        reactiveDeliveryRepository.save(testDelivery).subscribe();
    }

    @After
    public void tearDown() throws Exception {
        reactiveDeliveryRepository.deleteAll().subscribe();
    }

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
                .expectBody();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "wrong user ID")
    public void getlocationbyunauthedcustomer() throws Exception {

        this.webClient.get()
                .uri("/deliveries/customer/location/b2c24658-37d4-4d77-9cfc-cea77da59323")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody();
    }


    @Test
    @WithMockUser(roles = "BRANCH_WORKER")
    public void checkOutDelivery() throws Exception {
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deliveries/checkout")
                        .queryParam("id", "b2c24658-37d4-4d77-9cfc-cea77da59323")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(DeliveryCheckOut.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCourirerDeliveriesTodayTest() throws Exception {
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deliveries/today")
                        .queryParam("courierid", "a846ffb4-df13-44d6-8b2d-43d1c1fd2c47")
                        .queryParam("status", "ON_COURIER")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Delivery.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCourirerDeliveriesTodayUnexpectedStatusTest() throws Exception {
        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deliveries/today")
                        .queryParam("courierid", "a846ffb4-df13-44d6-8b2d-43d1c1fd2c47")
                        .queryParam("status", "ON_WAY_WAREHOUSE")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Delivery.class);
    }
}